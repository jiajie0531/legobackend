package com.delllogistics.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.AuthenticationException;

/**自定义JWT异常
 * Created by xzm on 2018-1-25.
 */
@Getter
@Setter
public class JwtAuthException extends AuthenticationException {
    private Integer code;
    public JwtAuthException(ServiceExceptionEnum serviceExceptionEnum) {
        super(serviceExceptionEnum.getMessage());
        this.code=serviceExceptionEnum.getCode();

    }
}
