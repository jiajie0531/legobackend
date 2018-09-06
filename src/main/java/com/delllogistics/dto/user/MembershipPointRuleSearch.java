package com.delllogistics.dto.user;

import com.delllogistics.dto.BaseSearchModel;
import com.delllogistics.entity.enums.MembershipPointType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 *  会员积分规则
 * Created by calvin  2018/4/25
 */
@Getter
@Setter
public class MembershipPointRuleSearch extends BaseSearchModel {

    /**
     * 积分类型
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipPointType membershipPointType;



}
