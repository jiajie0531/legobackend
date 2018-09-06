package com.delllogistics.service;

import com.delllogistics.dto.BackendUser;
import com.delllogistics.dto.Result;
import com.delllogistics.entity.enums.UserType;
import com.delllogistics.entity.user.User;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.SystemException;
import com.delllogistics.repository.user.UserRepository;
import com.delllogistics.service.sys.SysResourceService;
import com.delllogistics.util.EntityConvertUtil;
import com.delllogistics.util.JwtTokenUtil;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BackendUserService {


    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;

    private final SysResourceService sysResourceService;

    private final JwtTokenUtil jwtTokenUtil;



    @Autowired
    public BackendUserService(UserRepository userRepository, PasswordEncoder passwordEncoder, SysResourceService sysResourceService, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.sysResourceService = sysResourceService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public Page<User> findBackendUsers(int page, int size, User user) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        Specification<User> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Predicate p1 = criteriaBuilder.equal(root.get("type").as(Integer.class), UserType.ADMINISTRATOR.value);
            predicates.add(p1);
            if (!StringUtils.isEmpty(user.getUsername())) {
                Predicate p2 = criteriaBuilder.like(root.get("username"), "%" + user.getUsername() + "%");
                predicates.add(p2);
            }
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            predicates.add(criteriaBuilder.equal(root.get("company"), user.getCompany()));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return userRepository.findAll(specification, pageable);
    }

    /**
     * 保存后台用户
     *
     * @param user 后台用户
     */
    @CacheEvict(value="resources",key="#user.id")
    public void submitBackendUser(User user) {
        /*
         * 用户名唯一检查
         */
        User oldUser = userRepository.findByUsernameAndType(user.getUsername(), UserType.ADMINISTRATOR.value);
        if (StringUtils.isEmpty(user.getId())) {
            if (StringUtils.isEmpty(user.getPassword())) {
                throw new SystemException(ExceptionCode.PASSWORD_NULL, "密码不能为空");
            }
            if (oldUser != null) {
                throw new SystemException(ExceptionCode.USERNAME_EXISTS, "用户名已存在");
            }
            user.setType(UserType.ADMINISTRATOR.value);
            /*
             * 密码加密
             */
            user.setPassword(encodePassword(user.getPassword()));
            userRepository.save(user);
        } else {
            if (oldUser == null) {
                oldUser = userRepository.findOne(user.getId());
            } else {
                if (!oldUser.getId().equals(user.getId())) {
                    throw new SystemException(ExceptionCode.USERNAME_EXISTS, "用户名已存在");
                }
            }
            EntityConvertUtil.setFieldToEntity(user, oldUser, "password", "type", "company","token");
            userRepository.save(oldUser);
        }

    }

    /**
     * 密码加密
     *
     * @param password 明文密码
     * @return 加密后的密码
     */
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void delBackendUser(Long id) {
        userRepository.delete(id);
    }


    /**
     * 后台用户登录
     *
     * @param backendUser username和password为必须
     * @return 验证结果
     */
    @Transactional
    public Result login(BackendUser backendUser) {
        User userByName = userRepository.findByUsernameAndType(backendUser.getUsername(), UserType.ADMINISTRATOR.value);
        if (userByName == null) {
            return ResultUtil.error(-2, "用户不存在");
        }
        /*
          密码验证
         */
        boolean matches = passwordEncoder.matches(backendUser.getPassword(), userByName.getPassword());
        if (matches) {

            final String randomKey = jwtTokenUtil.getRandomKey();
            final String token = jwtTokenUtil.generateToken(userByName.getUsername(), randomKey);
            userByName.setToken(token);
            userRepository.save(userByName);

            backendUser=sysResourceService.getBackendUserAfterLogin(userByName);
            backendUser.setToken(token);
            backendUser.setRandomKey(randomKey);
            return ResultUtil.success(backendUser);
        } else {
            return ResultUtil.error(-3, "用户密码不正确");
        }
    }


    /**
     * 注销
     *
     * @param user 用户
     */
    public void logout(User user) {
        if (user != null) {
            user.setToken(null);
            userRepository.save(user);
        }
    }

    /**
     * 修改密码
     *
     * @param backendUser 密码
     */
    public void updatePassword(BackendUser backendUser) {
        if (StringUtils.isEmpty(backendUser.getPassword())) {
            throw new SystemException(ExceptionCode.PASSWORD_NULL, "密码不能为空");
        }
        User user = userRepository.findOne(backendUser.getId());
        user.setPassword(encodePassword(backendUser.getPassword()));
        userRepository.save(user);
    }

    /**
     * 页面刷新时根据token重新获取redux的内容
     *
     * @param user 用户
     * @return 用户权限
     */

    public BackendUser findResourcesByToken(User user) {
        user = userRepository.findOne(user.getId());
        return sysResourceService.getBackendUserAfterLogin(user);
    }

}
