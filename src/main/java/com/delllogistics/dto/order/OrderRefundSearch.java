package com.delllogistics.dto.order;

import com.delllogistics.dto.BaseSearchModel;
import com.delllogistics.entity.enums.OrderRefundStatus;
import com.delllogistics.entity.enums.OrderRefundType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class OrderRefundSearch extends BaseSearchModel {

    /**
     * 退单编号
     */
    private String orderRefundCode;
    /**
     * 退单ID
     */
    private Long orderRefundId;
    /**
     * 订单ID.订单编号
     */
    private String orderMainCode;

    /**
     * 收货人
     */
    private String consignee;


    /**
     * 手机号码
     */
    private String phone;

    /**
     * 服务类型
     */
    @Enumerated(EnumType.STRING)
    private OrderRefundType orderRefundType;

    /**
     * 订单售后状态
     */
    @Enumerated(EnumType.STRING)
    private OrderRefundStatus orderRefundStatus;


}
