package com.delllogistics.service;

import com.delllogistics.entity.sys.WechatConfig;
import com.github.wxpay.sdk.WXPayConfig;

import java.io.InputStream;

public class WXPayConfigImpl implements WXPayConfig {

    private WechatConfig weChatConfig;
    private static WXPayConfigImpl INSTANCE;
    private WXPayConfigImpl() {}

    public static WXPayConfigImpl getInstance(WechatConfig weChatConfig) throws Exception{
        if (INSTANCE == null) {
            synchronized (WXPayConfigImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WXPayConfigImpl();
                    INSTANCE.weChatConfig = weChatConfig;
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public String getAppID() {
        return weChatConfig.getAppId();
    }

    @Override
    public String getMchID() {
        return weChatConfig.getMchId();
    }

    @Override
    public String getKey() {
        return weChatConfig.getPaySecret();
    }

    @Override
    public InputStream getCertStream() {
        return null;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 2000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }
}
