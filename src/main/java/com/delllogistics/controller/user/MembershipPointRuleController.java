package com.delllogistics.controller.user;

import com.delllogistics.dto.Result;
import com.delllogistics.dto.user.MembershipPointRuleSearch;
import com.delllogistics.entity.user.MembershipPointRule;
import com.delllogistics.entity.user.User;
import com.delllogistics.service.user.MembershipPointRuleService;
import com.delllogistics.spring.annotation.CurrentUser;
import com.delllogistics.spring.annotation.LogAnnotation;
import com.delllogistics.spring.annotation.RestJsonController;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RestJsonController
@RequestMapping("/user/membershipPointRule")
public class MembershipPointRuleController {

    private final MembershipPointRuleService membershipPointRuleService;

    @Autowired
    public MembershipPointRuleController(MembershipPointRuleService membershipPointRuleService) {
        this.membershipPointRuleService = membershipPointRuleService;
    }

    /**
     * 查询会员积分规则
     *
     * @param membershipPointRuleSearch 查询条件
     * @return 返回结果集
     */
    @PostMapping("/findAll")
    @LogAnnotation(value = "查询会员积分规则")
    public Page<MembershipPointRule> findALl(@RequestBody MembershipPointRuleSearch membershipPointRuleSearch) {
        return membershipPointRuleService.findAll(membershipPointRuleSearch);
    }

    /**
     * 保存会员积分规则
     *
     * @param MembershipPointRule 会员积分规则
     * @param bindingResult       绑定结果
     * @return 返回结果
     */
    @PostMapping("/save")
    @LogAnnotation(value = "保存会员积分规则")
    public Result save(@Valid @RequestBody MembershipPointRule MembershipPointRule, BindingResult bindingResult, @CurrentUser User user) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(-1, bindingResult.getFieldError().getDefaultMessage());
        }
        membershipPointRuleService.save(MembershipPointRule, user);
        return ResultUtil.success();
    }


}
