package com.delllogistics.dto;

import com.delllogistics.entity.user.User;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PersonInfo {



    /**
     * 所有订单
     */
    private String allCount;
    /**
     * 成交订单数量
     */
    private String dealCount;

    /**
     * 已下订单数量
     */
    private String ordersCount;

    /**
     * 订单金额
     */
    private String ordersAmount;

    /**
     * 用户信息
     */
    private User user;

}
