package com.delllogistics.entity.enums;

/**
 *  订单退还物流类型
 * Created by calvin  2018/2/27
 */
public enum OrderRefundLogisticsType {
    ALL("all", 0, "全部"),
    EMPLOYEE("employee", 1, "师傅上门"),
    EXPRESS("express", 2, "快递"),
    PICK_UP("pick_up", 3, "自提");

    public String name;
    public int value;
    public String description;

    OrderRefundLogisticsType(String name, int value, String description) {
        this.name = name;
        this.value = value;
        this.description = description;
    }




}
