package com.delllogistics.controller;

import com.delllogistics.dto.Result;
import com.delllogistics.dto.WechatRegister;
import com.delllogistics.dto.app.AuthResponse;
import com.delllogistics.dto.user.UserSearch;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.user.MembershipRank;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.user.UserAccount;
import com.delllogistics.service.UserService;
import com.delllogistics.spring.annotation.*;
import com.delllogistics.util.ResultUtil;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestJsonController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/getAllUsers")
    public Page<User> getAllUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return userService.getUsersByPage(page, size);
    }


    /**
     * 根据用户名获取用户
     * @param userSearch 条件
     * @return 用户列表
     */
    @PostMapping("/getUserByName")
    @JsonConvert(
            type = User.class,
            includes = {"id", "username","photo"}
    )
    public Result getUserByName(@RequestBody UserSearch userSearch) {
        Page<User>  users = userService.getUserByName(userSearch);
        return ResultUtil.success(users);
    }


    /**
     * 微信授权登录
     *
     * @param appId 公众号appId
     * @param code  授权code
     * @return 登录结果
     */
    @RequestMapping(value = "/getUserByWechat", method = RequestMethod.POST)
    @JsonConvert(type = AuthResponse.class,
            nest = {
                    @NestConvert(type = UserAccount.class,includes = {"company","membershipRank"}),
                    @NestConvert(type = MembershipRank.class, includes = {"name", "discount"}),
                    @NestConvert(type = Company.class, includes = {"id", "name"})
            })
    @LogAnnotation(value="微信授权登录")
    public Result<AuthResponse> getUserBywechat(@RequestParam String appId, @RequestParam String code, WechatRegister wechatRegister) {
        AuthResponse authResponse = userService.getUserByWechat(appId, code,wechatRegister);
        return ResultUtil.success(authResponse);
    }

    @PostMapping("/bindCompany")
    @JsonConvert(type = UserAccount.class,includes = {"company","membershipRank"},
            nest = {
                    @NestConvert(type = MembershipRank.class, includes = {"name", "discount"}),
                    @NestConvert(type = Company.class, includes = {"id", "name"})
            })
    public Result bindCompany(@RequestParam  Long companyId, @CurrentUser User user){
        UserAccount userAccount = userService.bindCompany(companyId, user);
        return ResultUtil.success(userAccount);
    }


}
