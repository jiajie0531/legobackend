package com.delllogistics.dto.order;

import com.delllogistics.dto.BaseSearchModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OrderItemEvalueteSearch extends BaseSearchModel {

    /**
     * 商品Id
     */
    private Long goodsId;

    /**
     * 商品名
     */
    private String name;
    /**
     * 订单明细ID
     */
    private Long orderItemId;
    /**
     * 订单Id
     */
    private Long orderMainId;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 手机号码
     */
    private String phone;



}
