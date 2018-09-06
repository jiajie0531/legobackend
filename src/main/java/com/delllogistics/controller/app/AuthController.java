package com.delllogistics.controller.app;

import com.delllogistics.dto.Result;
import com.delllogistics.dto.app.AuthRequest;
import com.delllogistics.dto.app.AuthResponse;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.user.MembershipRank;
import com.delllogistics.entity.user.UserAccount;
import com.delllogistics.service.app.AuthService;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.NestConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 请求验证的
 *
 * @author fengshuonan
 * Date 2017/8/24 14:22
 */
@RestJsonController
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * PostMan获取权限
     *
     * @param authRequest 用户名密码
     * @return 结果
     */
    @RequestMapping(value = "${jwt.auth-path}")
    @JsonConvert(type = AuthResponse.class,
            nest = {
                    @NestConvert(type = UserAccount.class,includes = {"company","membershipRank"}),
                    @NestConvert(type = MembershipRank.class, includes = {"name", "discount"}),
                    @NestConvert(type = Company.class, includes = {"id", "name"})
            })
    public Result testToken(AuthRequest authRequest) {
        AuthResponse authResponse = authService.authUser(authRequest);
        return ResultUtil.success(authResponse);
    }
}
