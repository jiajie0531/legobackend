package com.delllogistics.dto;

import com.delllogistics.entity.user.WechatUser;
import lombok.Getter;
import lombok.Setter;

/**微信授权结果
 * Created by xzm on 2018-1-12.
 */
@Getter
@Setter
public class WechatAuthResult {
    private String token;
    private String companyId;
    private WechatUser wechatUser;
}
