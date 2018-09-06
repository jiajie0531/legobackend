package com.delllogistics.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

/**微信支付属性配置
 * Created by xzm on 2018-3-19.
 */
@Configuration
@ConfigurationProperties(prefix = WxPayProperties.WX_PAY_PREFIX)
@Getter
@Setter
public class WxPayProperties {

    public static final String WX_PAY_PREFIX = "wechat.pay";

    /**
     * 公众号APPID
     */
    private String appId;

    /**
     * 微信支付商户号
     */
    private String mchId;

    /**
     * 微信支付平台商户API密钥
     */
    private String mchKey;

    /**
     * 服务商模式下的子商户公众账号ID
     */
    private String subAppId;

    /**
     * 服务商模式下的子商户号
     */
    private String subMchId;

    /**
     * apiclient_cert.p12 证书文件的绝对路径
     */
    private String keyPath;

    /**
     * 支付有效时间(单位：分钟)
     */
    private int validTime;

    /**
     * 支付结果通知Url
     */
    private String payNotifyUrl;

    /**
     * 退款结果通知Url
     */
    private String refundNotifyUrl;

    /**
     * 是否使用测试沙盒环境
     */
    private boolean useSandboxEnv=false;

    /**
     * 沙盒环境测试支付金额
     */
    private BigDecimal sanboxEnvPayFee;

    /**
     * 是否使用1分钱测试支付
     */
    private boolean useTestPay=false;
    /**
     * 是否使用本地开发环境，不发起微信支付
     */
    private boolean useDevEnv=false;
}
