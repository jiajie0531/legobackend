package com.delllogistics.entity.enums;

/**
 *  退款申请类型
 * Created by calvin  2018/1/15
 */
public enum OrderRefundType {
    ALL("all", 0, "全部"),
    ONLY_REFUND("only_refund", 1, "仅退款"),
    REFUND_GOODS("refund_goods", 2, "退货退款"),
    EXCHANGING_GOODS("exchanging_goods", 3, "换货");

    public String name;
    public int value;
    public String description;

    OrderRefundType(String name, int value, String description) {
        this.name = name;
        this.value = value;
        this.description = description;
    }


}
