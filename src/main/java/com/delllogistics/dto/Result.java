package com.delllogistics.dto;

import com.delllogistics.exception.ServiceExceptionEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**返回结果
 * Created by xzm on 2017/6/5.
 */
@Getter
@Setter
@ToString
public class Result<T> {
    /**
     * 结果码
     */
    private int code;
    /**
     * 提示信息
     */
    private String msg;
    /**
     *具体内容
     */
    private T data;

    public Result(){}

    public Result(Integer code, String message) {
        this.code=code;
        this.msg=message;
    }

    public Result(ServiceExceptionEnum serviceExceptionEnum) {
        this.code=serviceExceptionEnum.getCode();
        this.msg=serviceExceptionEnum.getMessage();
    }
}
