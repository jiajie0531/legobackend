package com.delllogistics.security;

import com.delllogistics.dto.Result;
import com.delllogistics.exception.BizExceptionEnum;
import com.delllogistics.exception.JwtAuthException;
import com.delllogistics.util.HttpUtils;
import com.delllogistics.util.RenderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jiajie on 07/05/2017.
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private Logger logger= LoggerFactory.getLogger(getClass());

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        logger.error("权限验证失败,url:{}, msg:{}", HttpUtils.getRequestUrl(request),authException.getMessage());
        if(authException instanceof JwtAuthException){
            JwtAuthException jwtAuthException= (JwtAuthException) authException;
            Result result=new Result();
            result.setCode(jwtAuthException.getCode());
            result.setMsg(jwtAuthException.getMessage());
            RenderUtil.renderJson(response, result);
        }else{
            RenderUtil.renderJson(response, new Result<>(BizExceptionEnum.TOKEN_UNKNOWN_ERROR));
        }

    }


}
