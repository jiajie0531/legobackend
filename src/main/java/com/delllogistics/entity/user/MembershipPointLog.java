package com.delllogistics.entity.user;

import com.alibaba.fastjson.annotation.JSONField;
import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.enums.MembershipPointType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 会员积分来源记录
 * Created by calvin  2018/2/23
 */
@Entity
@Getter
@Setter
public class MembershipPointLog extends BaseModel {

    /**
     * 用户ID
     */
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch= FetchType.LAZY)
    private User user;

    /**
     * 消费积分 正数为增加，负数为减少
     */
    @Column(name="points", columnDefinition="Decimal(10,2) default '0.00'")
    private BigDecimal points;

    /**
     * 积分类型
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipPointType membershipPointType;

    /**
     * 消费描述
     */
    private String description;

    /**
     * 关联企业id
     */
    @OneToOne
    @JoinColumn(nullable = false)
    @JSONField(serialize = false)
    private Company company;


}
