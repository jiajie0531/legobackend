package com.delllogistics.entity.Finance;

import com.alibaba.fastjson.annotation.JSONField;
import com.delllogistics.entity.*;
import com.delllogistics.entity.enums.PayChannel;
import com.delllogistics.entity.enums.PayStatus;
import com.delllogistics.entity.enums.PayRefundChannel;
import com.delllogistics.entity.order.OrderMain;
import com.delllogistics.entity.order.OrderRefund;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;


/**
 *  财务退款单
 * Created by calvin  2018/1/14
 */
@Entity
@Getter
@Setter
@SQLDelete(sql = "update finance_refund set is_deleted=1,update_time=now() where id=? and version_=?")
public class FinanceRefund extends BaseModel {

    /**
     * 用户ID
     */
    @JoinColumn(name = "user_id",nullable = false)
    @ManyToOne(fetch= FetchType.LAZY)
    private User user;

    /**
     * 退款单号
     */
    @Column(length = 32,nullable = false)
    private String code;

    /**
     * 退款单
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_refund_id",nullable = false)
    private OrderRefund orderRefund;

    /**
     * 交易状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32,nullable = false)
    private PayStatus payStatus;

    /**
     * 支付时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(length = 32,nullable = false)
    private Date payTime;

    /**
     * 交易类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32,nullable = false)
    private PayRefundChannel payRefundChannel;

    /**
     * 交易渠道
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32,nullable = false)
    private PayChannel payChannel;

    /**
     * 交易代码（可关联第三方平台交易id）
     */
    private String transactionCode;

    /**
     * 支付金额
     */
    @Column(precision = 12, scale = 2)
    private BigDecimal payAmount;
    /**
     * 退款扣除金额
     */
    @Column(precision = 12, scale = 2)
    private BigDecimal chargeAmount;
    /**
     * 退款金额
     */
    @Column(precision = 12, scale = 2)
    private BigDecimal refundAmount;
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
