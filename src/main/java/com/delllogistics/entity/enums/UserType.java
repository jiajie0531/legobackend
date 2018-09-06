package com.delllogistics.entity.enums;

/**
 * Created by xzm on 2017/6/7.
 * 用户类型
 */

public enum UserType {

    ADMINISTRATOR("administrator",1),//管理员
    WECHATUSER("wechat_user",0);//微信用户

    public String name;
    public int value;

    UserType(String name, int value) {
        this.name = name;
        this.value = value;
    }
}
