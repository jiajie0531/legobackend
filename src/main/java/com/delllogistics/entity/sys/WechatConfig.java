package com.delllogistics.entity.sys;

import com.delllogistics.entity.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by jiajie on 11/04/2017.
 */
@Entity
@Getter
@Setter
public class WechatConfig extends BaseModel {

    /**
     * wechat应用的名称
     */
    @Column(name = "app_name")
    private String appName;

    /**
     * wechat应用的Id
     */
    @Column(name = "app_id")
    private String appId;

    /**
     * wechat应用的密码
     */
    @Column(name = "app_secret")
    private String appSecret;

    /**
     * wechatPay的密码
     */
    @Column(name = "pay_secret")
    private String paySecret;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 支付通知URL
     */
    private String notifyUrl;

}
