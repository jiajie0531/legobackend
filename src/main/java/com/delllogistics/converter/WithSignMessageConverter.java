package com.delllogistics.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.delllogistics.config.properties.JwtProperties;
import com.delllogistics.config.properties.RestProperties;
import com.delllogistics.dto.BaseTransferEntity;
import com.delllogistics.exception.BizExceptionEnum;
import com.delllogistics.exception.GeneralException;
import com.delllogistics.security.DataSecurityAction;
import com.delllogistics.support.HttpKit;
import com.delllogistics.util.JwtTokenUtil;
import com.delllogistics.util.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Type;

/**
 * 带签名的http信息转化器
 *
 * @author fengshuonan
 */
@Component
public class WithSignMessageConverter extends FastJsonHttpMessageConverter {

    private final JwtProperties jwtProperties;

    private final JwtTokenUtil jwtTokenUtil;

    private final DataSecurityAction dataSecurityAction;

    private final RestProperties restProperties;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public WithSignMessageConverter(JwtProperties jwtProperties, JwtTokenUtil jwtTokenUtil, DataSecurityAction dataSecurityAction, RestProperties restProperties) {
        this.jwtProperties = jwtProperties;
        this.jwtTokenUtil = jwtTokenUtil;
        this.dataSecurityAction = dataSecurityAction;
        this.restProperties = restProperties;
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        InputStream in = inputMessage.getBody();
        if (restProperties.isSignOpen()) {
            Object o = JSON.parseObject(in, super.getFastJsonConfig().getCharset(), BaseTransferEntity.class, super.getFastJsonConfig().getFeatures());
            //先转化成原始的对象
            BaseTransferEntity baseTransferEntity = (BaseTransferEntity) o;

            //校验签名
            String token = HttpKit.getRequest().getHeader(jwtProperties.getHeader()).substring(7);
            String md5KeyFromToken = jwtTokenUtil.getMd5KeyFromToken(token);

            String object = baseTransferEntity.getObject();
            String json = dataSecurityAction.unlock(object);
            String encrypt = MD5Util.encrypt(object + md5KeyFromToken);

            if (!encrypt.equals(baseTransferEntity.getSign())) {
                logger.error("签名校验失败");
                throw new GeneralException(BizExceptionEnum.SIGN_ERROR);
            }
            //校验签名后再转化成应该的对象
            return JSON.parseObject(json, type);
        } else {
            return JSON.parseObject(in, super.getFastJsonConfig().getCharset(), type, super.getFastJsonConfig().getFeatures());

        }

    }


}
