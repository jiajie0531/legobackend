package com.delllogistics.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by jiajie on 15/04/2017.
 */
@Getter
@Setter
public class WechatJsConfig {
    /**
     * 应用id
     */
    private String appId;
    /**
     * 生成签名的时间戳
     */
    private long timestamp;
    /**
     * 生成签名的随机串
     */
    private String nonceStr;
    /**
     * 签名
     */
    private String signature;
}
