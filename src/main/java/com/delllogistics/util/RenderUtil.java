package com.delllogistics.util;

import com.alibaba.fastjson.JSON;
import com.delllogistics.exception.GeneralException;
import com.delllogistics.exception.GeneralExceptionEnum;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**统一返回
 * Created by xzm on 2018-1-18.
 */
public class RenderUtil {

    /**
     * 渲染json对象
     */
    public static void renderJson(HttpServletResponse response, Object jsonObject) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(jsonObject));
        } catch (IOException e) {
            throw new GeneralException(GeneralExceptionEnum.WRITE_ERROR);
        }
    }
}
