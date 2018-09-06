package com.delllogistics.entity.order;

import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.enums.OrderRefundLogisticsType;
import com.delllogistics.entity.logistics.LogisticsExpress;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 *  订单发货
 * Created by calvin  2018/1/29
 */
@Entity
@Getter
@Setter
public class OrderRefundLogistics extends BaseModel {


    /**
     * 取件地址
     */
    @ManyToOne(fetch =FetchType.LAZY ,cascade={CascadeType.PERSIST})
    @JoinColumn
    private DeliveryAddress fetchAddress;

    /**
     * 取件时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date fetchTime;


    /**
     * 收货地址
     */
    @ManyToOne(fetch =FetchType.LAZY ,cascade={CascadeType.PERSIST})
    @JoinColumn
    private DeliveryAddress shippingAddress;


    /**
     * 用户信息
     */
    @OneToOne
    @JoinColumn(nullable = false)
    private User user;

    /**
     * 物流公司
     */
    @OneToOne
    @JoinColumn
    private LogisticsExpress logisticsExpress;

    /**
     * 物流单号
     */
    private String logisticsCode;

    /**
     * 物流类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    @Valid
    private OrderRefundLogisticsType type;

    /**
     * 备注
     */
    //@Size(min = 1, max = 200, message = "备注长度必须在1和200之间")
    @Column(length = 200)
    private String remarks;



    

}
