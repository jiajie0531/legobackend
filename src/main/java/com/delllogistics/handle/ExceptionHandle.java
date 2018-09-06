package com.delllogistics.handle;

import com.alibaba.druid.util.StringUtils;
import com.delllogistics.dto.Result;
import com.delllogistics.exception.BizExceptionEnum;
import com.delllogistics.exception.GeneralException;
import com.delllogistics.exception.ServiceException;
import com.delllogistics.exception.SystemException;
import com.delllogistics.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;

/**
 * Created by xzm on 2017/6/7.
 */
@RestControllerAdvice
public class ExceptionHandle {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @ExceptionHandler(GeneralException.class)
    public Result handle(GeneralException e) {
        logger.error("系统级别捕捉GeneralException异常:{}", e.getMessage());
        return ResultUtil.error(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(SystemException.class)
    public Result handle(SystemException e) {
        logger.error("系统级别捕捉SystemException异常:{}", e.getMessage());
        return ResultUtil.error(e.getCode().getCodeNumber(),e.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    public Result handle(ServiceException e) {
        logger.error("系统级别捕捉ServiceException异常:{}", e.getMessage());
        return ResultUtil.error(e.getCode().getCodeNumber(),e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result handle(Exception e) {
        if(e instanceof SQLException) {
            logger.error("数据库操作异常", e);
        }else if(e instanceof DataIntegrityViolationException) {
            String  exName =   e.getCause().getClass().getName();
            String  exMessage =   e.getCause().getCause().getMessage();
            if(exName.contains("ConstraintViolationException")){
                if(exMessage.contains("Duplicate")){
                    String   duplicateStr = StringUtils.subString(exMessage,"entry","for");
                    return ResultUtil.error(20001,"数据重复：\r\n"+duplicateStr);
                }
            }
        }else{
            logger.error("未知异常", e);
        }


       /* Duplicate entry '顺丰快递' for key 'UK_62x67k3xu897s6vx0n5snfkpj'*/

        return ResultUtil.error(-100,e.getMessage());
    }






}
