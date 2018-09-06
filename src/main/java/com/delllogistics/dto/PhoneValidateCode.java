package com.delllogistics.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by jiajie on 08/07/2017.
 */
@Getter
@Setter
public class PhoneValidateCode {
    /**
     * 注册用户ID
     */
    private Long userId;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 用户输入验证码
     */
    private int code;
    /**
     * 邀请人ID
     */
    private Long inviterId;
}
