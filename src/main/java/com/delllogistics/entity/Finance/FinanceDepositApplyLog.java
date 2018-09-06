package com.delllogistics.entity.Finance;

import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.enums.ApplyStatus;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.user.UserAccount;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 *  用户预存款申请记录
 * Created by calvin  2018/3/7
 */
@Entity
@Getter
@Setter
@SQLDelete(sql = "update finance_deposit_apply_log set is_deleted=1,update_time=now() where id=? and version_=?")
public class FinanceDepositApplyLog extends BaseModel {


    /**
     * 操作用户
     */
    @JoinColumn(name = "action_user_id",nullable = false)
    @OneToOne(fetch=FetchType.LAZY)
    private User actionUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="finance_deposit_apply_id",nullable = false)
    @Valid
    private FinanceDepositApply financeDepositApply;

    /**
     * 目标用户
     */
    @JoinColumn(name = "user_account_id",nullable = false)
    @OneToOne(fetch=FetchType.LAZY)
    private UserAccount userAccount;

    /**
     * 充值金额
     */
    @DecimalMin(value = "0.00", message = "可用金额必须大于0")
    @Column(precision = 12, scale = 2,nullable = false)
    private BigDecimal amount;


    /**
     * 申请状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32,nullable = false)
    private ApplyStatus applyStatus;


    /**
     * 备注
     */
    @Size(min = 1, max = 200, message = "备注长度必须在1和200之间")
    @Column(length = 200)
    private String remarks;

}
