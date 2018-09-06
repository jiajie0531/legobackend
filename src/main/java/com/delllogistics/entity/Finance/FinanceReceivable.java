package com.delllogistics.entity.Finance;

import com.alibaba.fastjson.annotation.JSONField;
import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.order.OrderMain;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.enums.PayChannel;
import com.delllogistics.entity.enums.PayStatus;
import com.delllogistics.entity.enums.PayType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;


/**
 *  财务收款单
 * Created by calvin  2018/1/14
 */
@Entity
@Getter
@Setter
@SQLDelete(sql = "update finance_receivable set is_deleted=1,update_time=now() where id=? and version_=?")
public class FinanceReceivable extends BaseModel {

    /**
     * 用户ID
     */
    @JoinColumn(name = "user_id",nullable = false)
    @ManyToOne(fetch= FetchType.LAZY)
    private User user;

    /**
     * 收款单编号
     */
    private String code;

    /**
     * 订单ID
     */
    @JoinColumn(name = "order_main_id",nullable = false)
    @ManyToOne(fetch= FetchType.LAZY)
    private OrderMain orderMain;


    /**
     * 交易类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private PayType payType;

    /**
     * 交易渠道
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private PayChannel payChannel;

    /**
     * 交易代码（可关联第三方平台交易id）
     */
    private String transactionCode;
    /**
     * 交易状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private PayStatus payStatus;


    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 支付时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date payTime;

    /**
     * 等待支付金额
     */
    @Column(precision = 12, scale = 2)
    private BigDecimal waitingPayAmount;

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
