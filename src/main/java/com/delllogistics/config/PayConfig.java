package com.delllogistics.config;

import com.delllogistics.config.properties.WxPayProperties;
import com.delllogistics.wechatPay.MyWxPayServiceImpl;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** 微信支付
 * Created by xzm on 2018-3-19.
 */
@Configuration
public class PayConfig {

    private final WxPayProperties wxPayProperties;

    @Autowired
    public PayConfig(WxPayProperties wxPayProperties) {
        this.wxPayProperties = wxPayProperties;
    }

    private WxPayConfig payConfig() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(wxPayProperties.getAppId());
        payConfig.setMchId(wxPayProperties.getMchId());
        payConfig.setMchKey(wxPayProperties.getMchKey());
        payConfig.setSubAppId(wxPayProperties.getSubAppId());
        payConfig.setSubMchId(wxPayProperties.getSubMchId());
        payConfig.setKeyPath(wxPayProperties.getKeyPath());
        payConfig.setUseSandboxEnv(wxPayProperties.isUseSandboxEnv());

        return payConfig;
    }

    @Bean
    public WxPayService payService() throws WxPayException {
        WxPayService payService = new MyWxPayServiceImpl();
        payService.setConfig(payConfig());
        return payService;
    }

}
