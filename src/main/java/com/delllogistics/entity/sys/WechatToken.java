package com.delllogistics.entity.sys;

import com.delllogistics.entity.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by jiajie on 15/04/2017.
 */
@Entity
@Getter
@Setter
public class WechatToken extends BaseModel {

    /**
     * wechat应用的Id
     */
    @Column(name = "app_id")
    private String appId;

    /**
     * 凭证类型
     */
    @Column(name = "token_type")
    private int tokenType;

    /**
     * wechat接口调用凭证
     */
    @Column(name = "token")
    private String token;

    /**
     * token失效时间
     */
    @Column(name = "expired_at")
    private Date expiredAt;
}
