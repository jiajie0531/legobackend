package com.delllogistics.controller.user;

import com.delllogistics.dto.user.MembershipPromotionLogSearch;
import com.delllogistics.entity.user.MembershipPromotionLog;
import com.delllogistics.service.user.MembershipPromotionService;
import com.delllogistics.spring.annotation.LogAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 会员推广返利.<br/>
 * User: jiajie<br/>
 * Date: 11/03/2018<br/>
 * Time: 2:30 PM<br/>
 */
@RestController
@RequestMapping("/user/membershipPromotionLog")
public class MembershipPromotionLogController {

    private final MembershipPromotionService membershipPromotionService;

    @Autowired
    public MembershipPromotionLogController(MembershipPromotionService membershipPromotionService) {
        this.membershipPromotionService = membershipPromotionService;
    }

    /**
     * 查询会员推广返利
     * @param membershipPromotionLogSearch 查询条件
     * @return 返回结果集
     */
    @PostMapping("/findAll")
    @LogAnnotation(value="查询会员推广返利")
    public Page<MembershipPromotionLog> findAll(@RequestBody MembershipPromotionLogSearch membershipPromotionLogSearch) {
        return membershipPromotionService.findAll(membershipPromotionLogSearch);
    }
}
