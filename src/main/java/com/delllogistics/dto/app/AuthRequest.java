package com.delllogistics.dto.app;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** 登录验证
 * Created by xzm on 2018-1-26.
 */
@Getter
@Setter
@ToString
public class AuthRequest {
    private String username;
    private String password;
}
