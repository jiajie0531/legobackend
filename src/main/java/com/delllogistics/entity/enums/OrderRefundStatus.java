package com.delllogistics.entity.enums;

/**
 * 订单售后状态
 * Created by calvin  2018/1/15
 */
public enum OrderRefundStatus {

    ALL("all", 0, "全部"),
    RENT("missing", 1, "已再租"),//
    WAIT_SURRENDER("wait_surrender", 2, "等待退租"),//APP
    WAIT_SELLER_AGREE("wait_surrender", 3, "买家已经申请售后，等待卖家确认"),
    WAIT_BUYER_CONFIRM("wait_buyer_confirm", 4, "卖家已确认，等待买家确认"),
    WAIT_SELLER_CONFIRM("wait_seller_confirm", 5, "买家已确认,等待卖家确认退款"),
    SUCCESS("success", 6, "退款成功"),
    REFUNDING("refunding", 7, "退款中"),
    SELLER_SEND_GOODS("seller_send_goods", 8, "卖家已发货");

    public String name;
    public int value;
    public String description;

    OrderRefundStatus(String name, int value, String description) {
        this.name = name;
        this.value = value;
        this.description = description;
    }



}
