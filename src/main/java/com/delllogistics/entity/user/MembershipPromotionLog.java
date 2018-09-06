package com.delllogistics.entity.user;

import com.alibaba.fastjson.annotation.JSONField;
import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.sys.Company;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 推广返利记录.<br/>
 * User: jiajie<br/>
 * Date: 11/03/2018<br/>
 * Time: 1:37 PM<br/>
 */
@Entity
@Getter
@Setter
public class MembershipPromotionLog extends BaseModel {

    /**
     * 推荐人
     */
    @JoinColumn(name = "referrer_id",nullable = false)
    @ManyToOne(fetch= FetchType.LAZY)
    private User referrer;

    /**
     * 被推荐人
     */
    @JoinColumn(name = "recommended_id",nullable = false)
    @ManyToOne(fetch= FetchType.LAZY)
    private User recommended;

    /**
     * 赠送积分
     */
    @Column(name="points", columnDefinition="Decimal(10,2) default '0.00'")
    private BigDecimal points;

    /**
     * 关联企业id
     */
    @OneToOne
    @JoinColumn(nullable = false)
    @JSONField(serialize = false)
    private Company company;



}
