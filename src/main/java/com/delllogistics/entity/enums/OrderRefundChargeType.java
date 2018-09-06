package com.delllogistics.entity.enums;

/**
 *  退款单扣款类型
 * Created by calvin  2018/1/15
 */
public enum OrderRefundChargeType {
    ALL("all", 0, "全部"),
    MISSING("missing", 1, "物件缺失"),
    BREAKDOWN("breakdown", 2, "物件损坏"),
    OTHER("other", 3, "其他");

    public String name;
    public int value;
    public String description;

    OrderRefundChargeType(String name, int value, String description) {
        this.name = name;
        this.value = value;
        this.description = description;
    }


}
