package com.delllogistics.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

/**
 * 下啦列表自定义数据
 */
@Setter
@Getter
public class BaseSelect {
    /**
     * 值
     */
    private String id;
    /**
     * 名称
     */
    private String name;
}
