package com.delllogistics.dto.user;

import com.delllogistics.dto.BaseSearchModel;
import com.delllogistics.entity.user.MembershipRank;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * 会员列表搜索条件
 */
@Getter
@Setter
public class MembershipSearch extends BaseSearchModel {
    /**
     * 会员等级
     */
    private MembershipRank membershipRank;




}
