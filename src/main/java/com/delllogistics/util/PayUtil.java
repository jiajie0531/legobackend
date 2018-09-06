package com.delllogistics.util;

import com.delllogistics.config.properties.WxPayProperties;

import java.math.BigDecimal;

/**
 * 支付工具类
 */
public class PayUtil {

    public static BigDecimal setOrderAmount(WxPayProperties wxPayProperties, BigDecimal orderAmount,int num) {
        if(wxPayProperties.isUseSandboxEnv()){
            orderAmount=wxPayProperties.getSanboxEnvPayFee();
        }else if(wxPayProperties.isUseTestPay()){
            orderAmount=new BigDecimal(0.01*num+"");
        }
        return orderAmount;
    }

}
