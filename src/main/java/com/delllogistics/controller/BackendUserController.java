package com.delllogistics.controller;

import com.delllogistics.dto.BackendUser;
import com.delllogistics.dto.Result;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.sys.SysResourceMenu;
import com.delllogistics.entity.sys.SysResourceRole;
import com.delllogistics.entity.user.User;
import com.delllogistics.service.BackendUserService;
import com.delllogistics.spring.annotation.*;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@RestJsonController
@RequestMapping("/backendUser")
public class BackendUserController {

    private final BackendUserService backendUserService;

    @Autowired
    public BackendUserController(BackendUserService backendUserService) {
        this.backendUserService = backendUserService;
    }

    @PostMapping("/findBackendUsers")
    @JsonConvert(type = User.class,
            includes = {"id","username","createTime","roles","companies"},
            nest = {@NestConvert(type=SysResourceRole.class,includes = {"id"}),@NestConvert(type=Company.class,includes = "id")})
    public Page<User> findBackendUsers(int page,int size,User user){
        return backendUserService.findBackendUsers(page, size, user);
    }

    /**
     * 新增或修改后台用户
     * @param user 后台用户
     * @param bindingResult 属性检测
     * @return 结果
     */
    @PostMapping("/submitBackendUser")
    @LogAnnotation(value="编辑后台用户")
    public Result submitBackendUser(@Valid User user, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResultUtil.error(-1,bindingResult.getFieldError().getDefaultMessage());
        }
        backendUserService.submitBackendUser(user);
        return ResultUtil.success();
    }

    @PostMapping("/delBackendUser")
    public Result delBackendUser(@RequestParam Long id){
        backendUserService.delBackendUser(id);
        return ResultUtil.success();
    }

    /**
     * 后台用户登录
     * @param backendUser 用户名和密码
     * @param bindingResult 非空检测
     * @return 登录结果
     */
    @PostMapping("/login")
    @LogAnnotation(value="登录")
    @JsonConvert(type = BackendUser.class,nest = {
            @NestConvert(type = SysResourceMenu.class,includes = {"id","name","icon","subMenus","url"}),
            @NestConvert(type = Company.class,includes = {"id","name"})
    })
    public Result login(@Valid BackendUser backendUser, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResultUtil.error(-1,bindingResult.getFieldError().getDefaultMessage());
        }
        return backendUserService.login(backendUser);
    }

    /**
     * 注销
     *
     * @param user 当前用户
     * @return 注销结果
     */
    @PostMapping("/logout")
    @LogAnnotation(value="注销")
    public Result logout(@CurrentUser User user) {
        backendUserService.logout(user);
        return ResultUtil.success();
    }

    /**
     * 修改密码
     * @param backendUser 用户ID和密码
     * @return 修改结果
     */
    @PostMapping("/updatePassword")
    @LogAnnotation(value="修改密码")
    public Result updatePassword(BackendUser backendUser){
        backendUserService.updatePassword(backendUser);
        return ResultUtil.success();
    }

    /**
     * 根据token重新获取登录后的redux信息
     * @param user 用户令牌
     * @return redux信息
     */
    @PostMapping("/findResourcesByToken")
    @JsonConvert(type = BackendUser.class,nest = {
            @NestConvert(type = SysResourceMenu.class,includes = {"id","name","icon","subMenus","url"}),
            @NestConvert(type = Company.class,includes = {"id","name"})
    })
    public Result<BackendUser> findResourcesByToken(@CurrentUser User user){
        BackendUser backendUser=backendUserService.findResourcesByToken(user);
        return ResultUtil.success(backendUser);
    }
}
