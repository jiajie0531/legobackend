package com.delllogistics.entity.enums;

/**
 * 订单物流类型.<br/>
 * User: jiajie<br/>
 * Date: 23/12/2017<br/>
 * Time: 9:36 AM<br/>
 */
public enum OrderLogisticsType {
    ALL("all", 0, "全部"),
    EMPLOYEE("employee", 1, "师傅上门"),
    EXPRESS("express", 2, "快递"),
    PICK_UP("pick_up", 3, "自提");

    public String name;
    public int value;
    public String description;

    OrderLogisticsType(String name, int value, String description) {
        this.name = name;
        this.value = value;
        this.description = description;
    }




}
