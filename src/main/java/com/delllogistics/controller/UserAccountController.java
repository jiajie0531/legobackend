package com.delllogistics.controller;

import com.delllogistics.dto.BaseSearchModel;
import com.delllogistics.dto.Result;
import com.delllogistics.dto.user.UserSearch;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.user.UserAccount;
import com.delllogistics.service.UserAccountService;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.NestConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestJsonController
@RequestMapping("/userAccount")
public class UserAccountController {

    private final UserAccountService userAccountService;

    @Autowired
    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PostMapping("/getUserAccountByName")
    @JsonConvert(
            type = UserAccount.class,
            includes = {"id", "user"},
            nest = {@NestConvert(type=User.class,includes = {"username","photo"})}
    )
    public Result getUserAccountByName(@RequestBody BaseSearchModel userSearch) {
        Page<UserAccount> users = userAccountService.getUserAccountByName(userSearch);
        return ResultUtil.success(users);
    }
}
