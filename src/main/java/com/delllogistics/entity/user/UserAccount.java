package com.delllogistics.entity.user;

import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.sys.Company;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id","company_id"})})
@Getter
@Setter
public class UserAccount extends BaseModel{


    /**
     * 消费积分
     */
    private BigDecimal payPoints = BigDecimal.ZERO;

    /**
     * 会员等级积分
     */
    private BigDecimal rankPoints = BigDecimal.ZERO;


    /**
     * 用户现有资金
     */
    private BigDecimal userMoney = BigDecimal.ZERO;

    /**
     * 用户冻结资金
     */
    private BigDecimal frozenMoney = BigDecimal.ZERO;

    /**
     * 当前账户积累的金额
     */
    private BigDecimal deposit = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    private MembershipRank membershipRank;

}
