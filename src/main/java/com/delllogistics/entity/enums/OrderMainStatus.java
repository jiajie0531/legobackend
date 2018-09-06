package com.delllogistics.entity.enums;

/**
 * status of order.<br/>
 * User: jiajie<br/>
 * Date: 17/11/2017<br/>
 * Time: 7:25 AM<br/>
 */
public enum OrderMainStatus {
    /**
     * 查询全部
     */
    ALL,
    /**
     * 续租
     */
    RENEW,
    /**
     * 等待买家付款
     */
    WAIT_TO_PAY,
    /**
     * 订单取消
     */
    CANCELED,
    /**
     * 已经支付
     */
    PAID,

    /**
     * 发起微信支付
     */
    WAIT_WECHAT_PAY,

    /**
     * 支付后被用户取消
     */
    CANCELED_AFTER_PAY_BY_USER,
    /**
     * 支付后被商家取消
     */
    CANCELED_AFTER_PAY_BY_VENUE,
    /**
     * 已发货等待确认
     */
    WAIT_TO_DELIVERY,
    /**
     * 已经送达
     */
    DELIVERY,
    /**
     * 完成
     */
    FINISHED;
}
