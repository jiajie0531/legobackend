package com.delllogistics.entity.logistics;

import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.enums.LogisticsDeliveryStatus;
import com.delllogistics.entity.enums.LogisticsDeliveryType;
import com.delllogistics.entity.order.OrderMain;
import com.delllogistics.entity.order.OrderRefund;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Size;

/**
 *  物流发货日志
 * Created by calvin  2018/1/29
 */
@Entity
@Getter
@Setter
public class LogisticsDeliveryLog extends BaseModel {

    /**
     * 关联订单
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name="order_main_id")
    @Valid
    private OrderMain orderMain;

    /**
     * 关联订单
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name="order_refund_id")
    private OrderRefund orderRefund;




    @OneToOne
    @JoinColumn
    private LogisticsExpress logisticsExpress;



    /**
     * 物流状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32,nullable = false)
    private LogisticsDeliveryStatus deliveryStatus;


    /**
     * 物流类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32,nullable = false)
    private LogisticsDeliveryType deliveryType;


    /**
     * 配送单号
     */
    private String logisticsCode;

    /**
     * 备注
     */
    @Size(min = 1, max = 200, message = "备注长度必须在1和200之间")
    @Column(length = 200)
    private String remarks;


}
