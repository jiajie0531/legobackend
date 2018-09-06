package com.delllogistics.entity.enums;

/**
 * 库存操作类型
 */
public enum InventoryOperate {
    PUTAWAY("putaway", 101, "商品上架"),
    UNRENT("unrent", 102, "退租"),
    OFFSHELF("offshelf", 103, "下架"),
    SALES("sales", 201, "销售"),
    UPDATESTOCK("updatestock", 301, "调整库存"),
    TIMEOUT_ORDER("timeoutOrder", 302, "释放订单"),
    CANCEL_ORDER("cancelOrder", 303, "取消订单");

    public String name;
    public int value;
    public String description;

    InventoryOperate(String name, int value, String description) {
        this.name = name;
        this.value = value;
        this.description = description;
    }
}
