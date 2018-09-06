package com.delllogistics.controller.app;

import com.delllogistics.dto.Result;
import com.delllogistics.entity.user.MembershipRank;
import com.delllogistics.service.user.MembershipRankService;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/appMemberShipRank")
public class AppMemberShipRankController {

    private final MembershipRankService membershipRankService;

    @Autowired
    public AppMemberShipRankController(MembershipRankService membershipRankService) {
        this.membershipRankService = membershipRankService;
    }

    @RequestMapping("/findMembershipRankList")
    @JsonConvert(type =MembershipRank.class,includes = {"name","icon"})
    public Result findMembershipRankList(@RequestParam Long companyId) {
        List<MembershipRank> rankList = membershipRankService.findAllSelect(companyId);
        return ResultUtil.success(rankList);
    }
}
