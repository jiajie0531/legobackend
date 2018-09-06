package com.delllogistics.controller.app;

import com.delllogistics.dto.Result;
import com.delllogistics.entity.user.MembershipPointRule;
import com.delllogistics.entity.user.MembershipRank;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.user.UserAccount;
import com.delllogistics.service.UserService;
import com.delllogistics.service.user.MembershipPointRuleService;
import com.delllogistics.service.user.MembershipPointService;
import com.delllogistics.service.user.MembershipRankService;
import com.delllogistics.spring.annotation.CurrentUser;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.NestConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 个人中心
 * Created by calvin  2018/1/23
 */
@RestJsonController
@RequestMapping("/app/user")
public class AppUserController {

    private final MembershipPointService membershipPointService;

    private final MembershipRankService membershipRankService;

    private final MembershipPointRuleService membershipPointRuleService;

    private final UserService userService;

    @Autowired
    public AppUserController(UserService userService, MembershipPointService membershipPointService, MembershipPointRuleService membershipPointRuleService, MembershipRankService membershipRankService) {
        this.userService = userService;
        this.membershipPointService = membershipPointService;
        this.membershipPointRuleService = membershipPointRuleService;
        this.membershipRankService = membershipRankService;
    }

    /**
     * 查询用户信息
     *
     * @param user 用户
     * @return 用户信息
     */
    @GetMapping("/findUserInfo")
    @JsonConvert(
            type = UserAccount.class,
            includes = {"user","membershipRank","payPoints","rankPoints","userMoney","frozenMoney"},
            nest = {
                    @NestConvert(type = User.class,includes = {"id", "username", "mobile","photo"}),
                    @NestConvert(type = MembershipRank.class, includes = {"icon", "name","maxPoints"})
            })
    public Result findUserInfo(@CurrentUser User user) {
        return ResultUtil.success(userService.findUserAcount(user));
    }

    /**
     * 获取个人积分来源
     *
     * @param user 用户
     * @return 个人积分来源列表-汇总-按照积分类型
     */
    @GetMapping("/findMembershipPointAggregate")
    public Result findMembershipPointAggregate(@CurrentUser User user) {
        return ResultUtil.success(membershipPointService.findPointAggregateList(user));
    }

    /**
     * 获取会员积分规则
     *
     * @param user 用户
     * @return 会员积分规则列表
     */
    @JsonConvert(
            type = MembershipPointRule.class,
            includes = {"id", "membershipPointType", "point"}
    )
    @GetMapping("/findMembershipPointRule")
    public Result findMembershipPointRule(@CurrentUser User user) {
        return ResultUtil.success(membershipPointRuleService.findMembershipPointRule(user));
    }

    /**
     * 获取会员等级信息
     *
     * @param user 用户
     * @return 会员等级信息
     */
    @JsonConvert(
            type = MembershipRank.class,
            includes = {"id", "name", "minPoints", "maxPoints","icon","description","discount"}
    )
    @GetMapping("/findMembershipRank")
    public Result findMembershipRank(Long companyId,@CurrentUser User user) {
        return ResultUtil.success(membershipRankService.findMembershipRank(companyId,user));
    }

}
