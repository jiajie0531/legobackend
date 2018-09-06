package com.delllogistics.service.sys;

import com.delllogistics.dto.BackendUser;
import com.delllogistics.dto.ResourceInfo;
import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.sys.SysResourceButton;
import com.delllogistics.entity.sys.SysResourceMenu;
import com.delllogistics.entity.sys.SysResourceRole;
import com.delllogistics.entity.user.User;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.SystemException;
import com.delllogistics.repository.sys.SysResourceButtonRepository;
import com.delllogistics.repository.sys.SysResourceMenuRepository;
import com.delllogistics.repository.sys.SysResourceRoleRepository;
import com.delllogistics.repository.user.UserRepository;
import com.delllogistics.util.ArrayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "resources")
public class SysResourceService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final SysResourceMenuRepository sysResourceMenuRepository;

    private final SysResourceButtonRepository sysResourceButtonRepository;

    private final SysResourceRoleRepository sysResourceRoleRepository;

    private final UserRepository userRepository;


    @Value("${qrcode.prev}")
    private String qrcodePrev;

    @Autowired
    public SysResourceService(SysResourceMenuRepository sysResourceMenuRepository, SysResourceButtonRepository sysResourceButtonRepository, SysResourceRoleRepository sysResourceRoleRepository, UserRepository userRepository) {
        this.sysResourceMenuRepository = sysResourceMenuRepository;
        this.sysResourceButtonRepository = sysResourceButtonRepository;
        this.sysResourceRoleRepository = sysResourceRoleRepository;
        this.userRepository = userRepository;
    }

    public ResourceInfo findCheckedResourcesByRoleId(Long roleId) {
        SysResourceRole sysResourceRole = sysResourceRoleRepository.findOne(roleId);
        Set<SysResourceButton> buttons = sysResourceRole.getButtons();
        Set<SysResourceMenu> menus = sysResourceRole.getMenus();
        ResourceInfo resourceInfo = new ResourceInfo();
        if (!ArrayUtil.isEmpty(buttons)) {
            Set<Long> buttonsId = buttons.stream().map(SysResourceButton::getId).collect(Collectors.toSet());
            resourceInfo.setButtons(buttonsId);

        }
        if (!ArrayUtil.isEmpty(menus)) {
            Set<Long> menusId = menus.stream().filter(menu -> ArrayUtil.isEmpty(menu.getChildren()) && ArrayUtil.isEmpty(menu.getButtons())).map(BaseModel::getId).collect(Collectors.toSet());
            resourceInfo.setMenus(menusId);
        }
        return resourceInfo;
    }

    public Set<SysResourceMenu> findAllResourceMenus(User user) {
        user = userRepository.findOne(user.getId());
        Set<SysResourceRole> sysResourceRoles = user.getRoles();
        /*
        admin角色返回所有权限，其余返回当前用户拥有的权限
         */
        for (SysResourceRole sysResourceRole : sysResourceRoles) {
            if (sysResourceRole.getName().equals("admin")) {
                return sysResourceMenuRepository.findByParentIsNullOrderBySort();
            }
        }
        Set<SysResourceMenu> menus = sysResourceMenuRepository.findByParentIsNullAndRolesInOrderBySort(sysResourceRoles);
        for (SysResourceMenu menu : menus) {
            findSubMenus(sysResourceRoles, menu);
        }

        return menus;
    }

    private void findSubMenus(Set<SysResourceRole> sysResourceRoles, SysResourceMenu menu) {
        Set<SysResourceMenu> childrenMenu = sysResourceMenuRepository.findByParentAndRolesInOrderBySort(menu, sysResourceRoles);
        menu.setChildren(childrenMenu);
        for (SysResourceMenu sysResourceMenu : childrenMenu) {
            if (StringUtils.isEmpty(sysResourceMenu.getUrl())) {
                findSubMenus(sysResourceRoles, sysResourceMenu);
            } else {
                Set<SysResourceButton> buttons = sysResourceButtonRepository.findByMenuAndRolesIn(sysResourceMenu, sysResourceRoles);
                sysResourceMenu.setButtons(buttons);
            }
        }
    }

    /**
     * 保存角色权限
     *
     * @param resourceInfo 菜单和按钮权限集合
     */
    @CacheEvict(allEntries = true)
    public void saveResources(ResourceInfo resourceInfo) {
        Set<Long> menusId = resourceInfo.getMenus();
        Set<Long> buttonsId = resourceInfo.getButtons();
        if (ArrayUtil.isEmpty(menusId) && ArrayUtil.isEmpty(buttonsId)) {
            throw new SystemException(ExceptionCode.RESOURCE_NULL, "权限不能为空!");
        }
        SysResourceRole sysResourceRole = sysResourceRoleRepository.findOne(resourceInfo.getRoleId());
        /*
          从按钮查询菜单权限
         */
        Set<SysResourceMenu> menuSet = new HashSet<>();
        Set<SysResourceButton> buttonSet = new HashSet<>();
        Iterable<SysResourceButton> buttons = sysResourceButtonRepository.findAll(buttonsId);
        for (SysResourceButton button : buttons) {
            buttonSet.add(button);
            SysResourceMenu menu = button.getMenu();
            menuSet.add(menu);
            if (menu.getParent() != null) {
                menuSet.add(menu.getParent());
            }
        }
        /*
        合并菜单权限
         */
        if (!ArrayUtil.isEmpty(menusId)) {
            Iterable<SysResourceMenu> resourceMenus = sysResourceMenuRepository.findAll(menusId);
            for (SysResourceMenu sysResourceMenu : resourceMenus) {
                menuSet.add(sysResourceMenu);
                if (sysResourceMenu.getParent() != null) {
                    menuSet.add(sysResourceMenu.getParent());
                }
            }
        }
        sysResourceRole.setButtons(buttonSet);
        sysResourceRole.setMenus(menuSet);
        sysResourceRoleRepository.save(sysResourceRole);
    }


    @Cacheable(key = "#user.id")
    public BackendUser getBackendUserAfterLogin(User user) {
        long start = System.currentTimeMillis();

            /*
            查询用户权限,合并角色权限
             */
        Set<String> permissions = new HashSet<>();
        Set<SysResourceRole> sysResourceRoles = user.getRoles();
        for (SysResourceRole sysResourceRole : sysResourceRoles) {
            if (!ArrayUtil.isEmpty(sysResourceRole.getButtons())) {
                Set<String> collect = sysResourceRole.getButtons().stream().map(SysResourceButton::getPermission).collect(Collectors.toSet());
                permissions.addAll(collect);
            }
        }
        Set<SysResourceMenu> menus = sysResourceMenuRepository.findByParentIsNullAndRolesInOrderBySort(sysResourceRoles);
        for (SysResourceMenu menu : menus) {
            Set<SysResourceMenu> childrenMenu = sysResourceMenuRepository.findByParentAndRolesInOrderBySort(menu, sysResourceRoles);
            menu.setSubMenus(childrenMenu);
        }
        BackendUser backendUser = new BackendUser();
        backendUser.setUsername(user.getUsername());
        backendUser.setId(user.getId());
        backendUser.setMenus(menus);
        backendUser.setPermissions(permissions);
        backendUser.setCompanies(user.getCompanies());
        backendUser.setQrcodePrev(qrcodePrev);
        long end = System.currentTimeMillis();
        logger.info("查询菜单耗时:{}毫秒", end - start);
        return backendUser;
    }

}
