package com.delllogistics.entity.enums;
/**
 *  交易状态
 * Created by calvin  2018/1/15
 */
public enum PayStatus {
    /**
     * 全部
     */
    ALL,

    /**
     * 等待支付
     */
    WAITING,
    /**
     * 支付金额异常
     */
    PAYAMOUNTWRONG,
    /**
     * 支付成功
     */
    SUCCESS,
    /**
     * 支付失败
     */
    FAILED,

    /**
     * 未支付
     */
    CANCEL,

    /**
     * 处理异常
     */
    ERROR,

    /**
     * 非法参数
     */
    INVALID,
    /**
     * 已付款至担保方
     */
    PROGRESS,
    /**
     * 超时
     */
    TIMEOUT,
    /**
     * 准备中
     */
    READY,
    /**
     * 支付中
     */
    PAYING;
}
