package com.delllogistics.dto.order;

import com.delllogistics.dto.BaseSearchModel;
import com.delllogistics.entity.enums.OrderRefundStatus;
import com.delllogistics.entity.enums.OrderLogisticsType;
import com.delllogistics.entity.enums.OrderMainStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
public class OrderMainSearch extends BaseSearchModel {


    private Long orderMainId;
    /**
     * 订单编号
     */
    private String code;
    /**
     * 收货人
     */
    private String consignee;


    /**
     * 手机号码
     */
    private String phone;

    /**
     * 配送类型
     */
    @Enumerated(EnumType.STRING)
    private OrderLogisticsType orderLogisticsType;




    /**
     * 订单状态
     */
    @Enumerated(EnumType.STRING)
    private OrderMainStatus status;


    /**
     * 订单售后状态
     */
    @Enumerated(EnumType.STRING)
    private OrderRefundStatus orderAfterSalesStatus;


}
