package com.delllogistics.dto.app;

import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.user.MembershipRank;
import com.delllogistics.entity.user.UserAccount;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 认证的响应结果
 * xzm
*/
@ToString
@Setter
@Getter
public class AuthResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    /**
     * jwt token
     */
    private final String token;

    /**
     * 用于客户端混淆md5加密
     */
    private final String randomKey;

    private UserAccount userAccount;


    public AuthResponse(String token, String randomKey,UserAccount userAccount) {
        this.token = token;
        this.randomKey = randomKey;
        this.userAccount=userAccount;
    }

    public AuthResponse(String token, String randomKey) {
        this.token = token;
        this.randomKey = randomKey;
    }
}
