package com.delllogistics.entity.user;

import com.alibaba.fastjson.annotation.JSONField;
import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.enums.MembershipPointType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class MembershipPointRule extends BaseModel {

    /**
     * 积分类型
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipPointType membershipPointType;

    /**
     * 奖励积分
     */
    @Column(nullable = false)
    private BigDecimal point;

    /**
     * 关联企业id
     */
    @OneToOne
    @JoinColumn(nullable = false)
    @JSONField(serialize = false)
    private Company company;
}
