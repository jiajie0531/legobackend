package com.delllogistics.entity.enums;

/**
 * Created by Administrator on 2017/9/8.
 */
public enum  OrderStatus {
    TODO("待完成",0),
    DONE("已完成",1);

    public String name;
    public int value;

    OrderStatus(String name, int value) {
        this.name = name;
        this.value = value;
    }
}
