package com.delllogistics.entity.enums;

/**
 * 库存类型代码
 */
public enum InventoryCode {
    ADDITION("addition", 0, "+"),
    SUBTRACTION("subtraction", 1, "-");

    public String name;
    public int value;
    public String description;

    InventoryCode(String name, int value, String description) {
        this.name = name;
        this.value = value;
        this.description = description;
    }
}
