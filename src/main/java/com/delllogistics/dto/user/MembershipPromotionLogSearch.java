package com.delllogistics.dto.user;

import com.delllogistics.dto.BaseSearchModel;
import com.delllogistics.entity.user.MembershipRank;
import lombok.Getter;
import lombok.Setter;

/**
 * 会员推广返利查询条件
 * Created by calvin  2018/4/25
 */
@Getter
@Setter
public class MembershipPromotionLogSearch extends BaseSearchModel {


    /**
     * 推荐人
     */
    private String referrer;

    /**
     * 被推荐人
     */
    private String recommended;


}
