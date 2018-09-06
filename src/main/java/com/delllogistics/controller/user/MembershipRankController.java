package com.delllogistics.controller.user;

import com.delllogistics.dto.Result;
import com.delllogistics.dto.user.MembershipRankSearch;
import com.delllogistics.entity.user.MembershipRank;
import com.delllogistics.entity.user.User;
import com.delllogistics.service.user.MembershipRankService;
import com.delllogistics.spring.annotation.CurrentUser;
import com.delllogistics.spring.annotation.LogAnnotation;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 会员级别
 * Created by guangxingju on 2017/11/2.
 */
@RestController
@RequestMapping("/user/membershipRank")
public class MembershipRankController {

    private final MembershipRankService membershipRankService;

    @Autowired
    public MembershipRankController(MembershipRankService membershipRankService) {
        this.membershipRankService = membershipRankService;
    }

    /**
     * 会员等级
     *
     * @param membershipRankSearch 查询条件
     * @return 返回结果集
     */
    @PostMapping("/findAll")
    @LogAnnotation(value = "查询会员推广返利")
    public Page<MembershipRank> findAll(@RequestBody MembershipRankSearch membershipRankSearch) {
        return membershipRankService.findAll(membershipRankSearch);
    }

    /**
     * 保存会员等级
     *
     * @param membershipRank 会员等级
     * @param bindingResult  绑定结果
     * @param user           用户
     * @return 返回结果
     */
    @PostMapping("/save")
    public Result save(@Valid @RequestBody MembershipRank membershipRank, BindingResult bindingResult, @CurrentUser User user) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(-1, bindingResult.getFieldError().getDefaultMessage());
        }
        membershipRankService.save(membershipRank, user);
        return ResultUtil.success();
    }

    /**
     * 删除会员等级
     *
     * @param membershipRank 会员等级
     * @param user           用户
     * @return 返回结果
     */
    @PostMapping("/delete")
    @LogAnnotation(value = "删除会员等级")
    public Result delete(@RequestBody MembershipRank membershipRank, @CurrentUser User user) {
        membershipRankService.delete(membershipRank, user);
        return ResultUtil.success();
    }


}
