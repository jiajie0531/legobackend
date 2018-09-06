package com.delllogistics.dto;

import lombok.Getter;
import lombok.Setter;

/** 穿梭框通用基本信息
 * Created by xzm on 2017-11-17.
 */
@Setter
@Getter
public class BaseTransfer {
    private String key;
    private String name;

    public BaseTransfer(String key, String name){
        this.key=key;
        this.name=name;
    }
}
