package com.delllogistics.entity.Finance;

import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.user.UserAccount;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
/**
 *  用户预存款
 * Created by calvin  2018/3/7
 */
@Entity
@Getter
@Setter
@SQLDelete(sql = "update finance_deposit set is_deleted=1,update_time=now() where id=? and version_=?")
public class FinanceDeposit extends BaseModel {


    /**
     * 用户
     */
    @JoinColumn(name = "user_account_id",nullable = false)
    @OneToOne(fetch=FetchType.LAZY,cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private UserAccount userAccount;

    /**
     * 可用金额
     */
    @DecimalMin(value = "0.00", message = "可用金额必须大于0")
    @Column(precision = 12, scale = 2,nullable = false)
    private BigDecimal usableAmount;

    /**
     * 被冻结的资金
     */
    @Column(precision = 12, scale = 2,nullable = false)
    private BigDecimal frozenAmount;



}
