package com.delllogistics.entity.user;

import com.delllogistics.entity.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * Created by jiajie on 11/04/2017.
 */
@Getter
@Setter
@Entity
public class WechatUser extends BaseModel {
    /**
     * 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionId是唯一的。
     */
    private String unionId;
    /**
     * 用户的标识，对当前开发者帐号唯一
     */
    private String openId;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 性别
     */
    private String sex;
    /**
     * 国家
     */
    private String country;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 头像地址(在用户修改微信头像后，旧的微信头像URL将会失效，
     * 因此开发者应该自己在获取用户信息后，将头像图片保存下来，
     * 避免微信头像URL失效后的异常情况。)
     */
    private String headImageUrl;
    /**
     * 微信用户IP
     */
    private String spbillCreateIp;

}
