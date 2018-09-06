package com.delllogistics.controller.user;

import com.delllogistics.dto.user.MembershipPointLogSearch;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.user.MembershipPointLog;
import com.delllogistics.entity.user.User;
import com.delllogistics.service.user.MembershipPointService;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.LogAnnotation;
import com.delllogistics.spring.annotation.NestConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 会员积分.<br/>
 * User: jiajie<br/>
 * Date: 11/03/2018<br/>
 * Time: 2:29 PM<br/>
 */
@RestJsonController
@RequestMapping("/user/membershipPoint")
public class MembershipPointController {

    private final MembershipPointService membershipPointService;

    @Autowired
    public MembershipPointController(MembershipPointService membershipPointService) {
        this.membershipPointService = membershipPointService;
    }

    /**
     * 查询会员积分记录
     *
     * @param membershipPointLogSearch 查询条件
     * @return 返回结果集
     */
    @PostMapping("/findAll")
    @LogAnnotation(value = "查询会员积分")
    @JsonConvert(
            type = MembershipPointLog.class,
            includes = {"id","user","points","membershipPointType" ,"description","company","createTime"},
            nest = {
                    @NestConvert(type = User.class, includes = {"id", "username","photo"}),
                    @NestConvert(type = Company.class, includes = {"id"})
            })
    public Page<MembershipPointLog> findAll(@RequestBody MembershipPointLogSearch membershipPointLogSearch) {
        return membershipPointService.findAll(membershipPointLogSearch);
    }
}
