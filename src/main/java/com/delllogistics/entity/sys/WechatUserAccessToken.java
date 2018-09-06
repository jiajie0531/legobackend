package com.delllogistics.entity.sys;

import com.delllogistics.entity.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by jiajie on 05/05/2017.
 */
@Entity
@Getter
@Setter
public class WechatUserAccessToken extends BaseModel {
    /**
     * wechat应用的Id
     */
    @Column(name = "app_id")
    private String appId;

    /**
     * token失效时间
     */
    @Column(name = "expired_at")
    private Date expiredAt;

    /**
     * wechat接口调用凭证
     */
    @Column(name = "access_token")
    private String accessToken;

    /**
     * 凭证失效时间（秒）
     */
    @Column(name = "expires_in", nullable = false, columnDefinition = "int default 0")
    private int expiresIn;

    private String refreshToken;

    /**
     * 用户的标识，对当前开发者帐号唯一
     */
    private String openid;

    private String scope;

}
