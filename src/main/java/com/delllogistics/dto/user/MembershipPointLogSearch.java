package com.delllogistics.dto.user;

import com.delllogistics.dto.BaseSearchModel;
import com.delllogistics.entity.user.MembershipRank;
import lombok.Getter;
import lombok.Setter;

/**
 * 积分查询条件
 *
 * @author JiaJie
 * @create 2018-03-13 7:24 PM
 **/
@Getter
@Setter
public class MembershipPointLogSearch extends BaseSearchModel {

    /**
     * 会员等级
     */
    private MembershipRank membershipRank;
}
