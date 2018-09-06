package com.delllogistics.entity.enums;

/**
 * 资金操作类型
 * Created by calvin  2018/1/15
 */
public enum UserAccountProcessType {
    PAY("pay", 1, "充值"),
    WITHDRAW("withdraw", 2, "提现"),
    ADJUST("adjust", 3, "管理员调节");

    public String name;
    public int value;
    public String description;

    UserAccountProcessType(String name, int value, String description) {
        this.name = name;
        this.value = value;
        this.description = description;
    }


}
