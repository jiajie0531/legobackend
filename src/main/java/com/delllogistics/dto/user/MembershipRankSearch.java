package com.delllogistics.dto.user;

import com.delllogistics.dto.BaseSearchModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 会员推广返利查询条件
 * Created by calvin  2018/4/25
 */
@Getter
@Setter
public class MembershipRankSearch extends BaseSearchModel {



    /**
     * 会员等级名称
     */
    private String name;


}
