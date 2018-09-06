package com.delllogistics.exception;

/**
 * Created by jiajie on 09/06/2017.
 */
public class ServiceException extends GlobalException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(ExceptionCode code, String message) {
        super(code, message);
    }
}

