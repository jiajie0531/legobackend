package com.delllogistics.util;

import com.delllogistics.dto.Result;

/**结果工具类
 * Created by xzm on 2017/6/5.
 */
public class ResultUtil {

    public static<T> Result<T> success(T object) {
        return success(object,"成功");
    }
    public static<T> Result<T> success(T object,String msg) {
        Result<T> result = new Result<>();
        result.setCode(0);
        result.setMsg(msg);
        result.setData(object);
        return result;
    }
    public static Result success() {
        return success(null);
    }

    public static Result error(Integer code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }




}
