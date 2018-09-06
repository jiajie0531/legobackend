package com.delllogistics.util;

import com.delllogistics.config.properties.WxPayProperties;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;

/**微信工具类
 * Created by xzm on 2018-4-2.
 */
public class WechatUtil {

    /**
     * 设置沙箱密钥
     * @param wxPayProperties 微信支付配置文件
     * @param wxPayService 支付逻辑封装
     * @throws WxPayException 微信支付异常
     */
    public static void setSandboxSignKey(WxPayProperties wxPayProperties,WxPayService wxPayService) throws WxPayException {
        if(wxPayProperties.isUseSandboxEnv()){
            WxPayConfig config = wxPayService.getConfig();
            config.setMchKey(wxPayProperties.getMchKey());
            String sandboxSignKey = wxPayService.getSandboxSignKey();
            config.setMchKey(sandboxSignKey);
        }
    }
}
