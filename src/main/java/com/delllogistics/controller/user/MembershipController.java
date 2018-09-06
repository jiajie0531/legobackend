package com.delllogistics.controller.user;

import com.delllogistics.dto.user.MembershipSearch;
import com.delllogistics.entity.sys.SysAdvert;
import com.delllogistics.entity.user.MembershipRank;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.user.UserAccount;
import com.delllogistics.service.user.MembershipService;
import com.delllogistics.spring.annotation.CurrentUser;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.NestConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 会员管理.<br/>
 * User: jiajie<br/>
 * Date: 10/03/2018<br/>
 * Time: 12:24 PM<br/>
 */
@RestJsonController
@RequestMapping("/user/membership")
public class MembershipController {

    private final MembershipService membershipService;

    @Autowired
    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    /**
     * 查询会员列表
     * @param membershipSearch 查询条件
     * @return 返回结果集
     */
    @PostMapping("/findAll")
    @JsonConvert(
            type = UserAccount.class,
            includes = {"id", "createTime", "membershipRank", "userMoney", "frozenMoney", "rankPoints", "payPoints","user"},
            nest = {
                    @NestConvert(type = MembershipRank.class, includes = {"icon", "name"}),
                    @NestConvert(type = User.class, includes = {"username", "photo","mobile","gender","city"})
            })
    public Page<UserAccount> findAll(@RequestBody MembershipSearch membershipSearch ) {
        return membershipService.findAll(membershipSearch);
    }



}
