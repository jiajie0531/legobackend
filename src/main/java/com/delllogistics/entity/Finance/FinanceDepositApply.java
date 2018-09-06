package com.delllogistics.entity.Finance;

import com.alibaba.fastjson.annotation.JSONField;
import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.enums.ApplyStatus;
import com.delllogistics.entity.user.UserAccount;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 *  用户预存款申请
 * Created by calvin  2018/3/7
 */
@Entity
@Getter
@Setter
@SQLDelete(sql = "update finance_deposit_apply set is_deleted=1,update_time=now() where id=? and version_=?")
public class FinanceDepositApply extends BaseModel {


    /**
     * 申请用户
     */
    @JoinColumn(name = "action_user_id",nullable = false)
    @OneToOne(fetch=FetchType.LAZY)
    private User actionUser;

    /**
     * 预存款申请单号
     */
    @Column(nullable = false)
    private String code;

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
     * 交易状态
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private ApplyStatus applyStatus;


    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="finance_deposit_apply_id")
    private List<FinanceDepositApplyLog> financeDepositApplyLogs;

    /**
     * 关联企业
     */
    @NotNull(message = "关联企业不能为空")
    @Valid
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JSONField(serialize = false)
    private Company company;

}
