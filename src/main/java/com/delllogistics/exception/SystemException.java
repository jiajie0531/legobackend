package com.delllogistics.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SystemException extends GlobalException {

    private ExceptionCode code;
    private String message;

    public SystemException(String message) {
        super(message);
        this.message = message;
    }

    public SystemException(ExceptionCode code, String message) {
        super(code, message);
        this.code = code;
        this.message = message;
    }
}
