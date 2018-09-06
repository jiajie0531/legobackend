package com.delllogistics.entity.enums;

/**
 * Created by jiajie on 15/04/2017.
 */
public enum TokenType {

    ACCESS_TOKEN("access_token", 0, "接口调用token"),
    JS_API_TICKET("js_api_ticket", 1, "js-sdk调用token");

    public String name;
    public int value;
    public String description;

    TokenType(String name, int value, String description) {
        this.name = name;
        this.value = value;
        this.description = description;
    }
}

