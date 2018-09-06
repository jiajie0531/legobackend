package com.delllogistics.entity.Finance;

import com.alibaba.fastjson.annotation.JSONField;
import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.order.OrderMain;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.enums.PayChannel;
import com.delllogistics.entity.enums.PayStatus;
import com.delllogistics.entity.enums.PayType;
import com.delllogistics.entity.user.UserAccount;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


/**
 *  用户预存款记录
 * Created by calvin  2018/1/14
 */
@Entity
@Getter
@Setter
@SQLDelete(sql = "update finance_deposit_log set is_deleted=1,update_time=now() where id=? and version_=?")
public class FinanceDepositLog extends BaseModel {

    /**
     * 预存款记录单号
     */
    private String code;

    /**
     * 用户ID
     */
    @JoinColumn(name = "user_account_id",nullable = false)
    @ManyToOne(fetch= FetchType.LAZY)
    private UserAccount userAccount;

    /**
     * 订单ID
     */
    @JoinColumn(name = "order_main_id",nullable = false)
    @ManyToOne(fetch= FetchType.LAZY)
    private OrderMain orderMain;

    /**
     * 交易状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32,nullable = false)
    private PayStatus payStatus;

    /**
     * 交易类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32,nullable = false)
    private PayType payType;

    /**
     * 交易渠道
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32,nullable = false)
    private PayChannel payChannel;

    /**
     * 交易金额
     */
    @DecimalMin(value = "0.00", message = "交易金额必须大于0")
    @Column(precision = 12, scale = 2,nullable = false)
    private BigDecimal amount;

    /**
     * 可用金额
     */
    @Column(precision = 12, scale = 2,nullable = false)
    private BigDecimal balance;

    /**
     * 交易单号
     */
    private String transactionCode;



    /**
     * 消费描述
     */
    private String description;

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
