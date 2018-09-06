package com.delllogistics.controller;

import com.delllogistics.dto.ResourceInfo;
import com.delllogistics.dto.Result;
import com.delllogistics.entity.sys.SysResourceMenu;
import com.delllogistics.entity.user.User;
import com.delllogistics.service.sys.SysResourceService;
import com.delllogistics.spring.annotation.CurrentUser;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@RestJsonController
@RequestMapping("/resource")
public class ResourceController {

    private final SysResourceService sysResourceService;

    @Autowired
    public ResourceController(SysResourceService sysResourceService) {
        this.sysResourceService = sysResourceService;
    }

    /**
     * 查询角色已有的权限
     * @param roleId 角色ID
     * @return 已有menu权限ID和button权限ID
     */
    @PostMapping("/findCheckedResourcesByRoleId")
    public Result<ResourceInfo> findCheckedResourcesByRoleId(@RequestParam Long roleId) {
        return ResultUtil.success(sysResourceService.findCheckedResourcesByRoleId(roleId));
    }

    /**
     * 查询所有菜单
     *
     * @return 菜单树
     */
    @PostMapping("/findAllResourceMenus")
    @JsonConvert(type =SysResourceMenu.class,includes = {"id","name","icon","children","buttons"})
    public Result<Set<SysResourceMenu>> findAllResourceMenus(@CurrentUser User user) {
        Set<SysResourceMenu> menus = sysResourceService.findAllResourceMenus(user);
        return ResultUtil.success(menus);
    }


    @PostMapping("/saveResources")
    public Result saveResources(ResourceInfo resourceInfo) {
        sysResourceService.saveResources(resourceInfo);
        return ResultUtil.success();
    }


}
