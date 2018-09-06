package com.delllogistics.exception;

import lombok.Getter;
import lombok.Setter;

/** 统一异常
 * Created by xzm on 2018-1-18.
 */
@Setter
@Getter
public class GeneralException  extends RuntimeException{
    private Integer code;
    private String message;

    public GeneralException(ServiceExceptionEnum serviceExceptionEnum) {
        this.code = serviceExceptionEnum.getCode();
        this.message = serviceExceptionEnum.getMessage();
    }
    public GeneralException(ServiceExceptionEnum serviceExceptionEnum,String message) {
        this.code = serviceExceptionEnum.getCode();
        this.message = message;
    }



}
