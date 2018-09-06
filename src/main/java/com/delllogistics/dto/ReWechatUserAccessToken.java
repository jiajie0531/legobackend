package com.delllogistics.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by jiajie on 15/04/2017.
 */
@Getter
@Setter
public class ReWechatUserAccessToken {

    /**
     * 接口调用凭证
     */
    private String access_token;

    /**
     * 凭证失效时间（秒）
     */
    private int expires_in;

    private String refresh_token;

    /**
     * 用户的标识，对当前开发者帐号唯一
     */
    private String openid;

    private String scope;

}
