package com.delllogistics.wechat;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/** 微信支付结果解析测试
 * Created by xzm on 2018-4-2.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MyWxPayServiceTest {

    @Autowired
    private WxPayService wxPayService;

    @Test
    public void parseWechatPayResultXML() throws WxPayException {
        String xml="<xml>\n" +
                "  <openid><![CDATA[ofdJzwgR0hL3Ok4Fr6gXNR1M_3Pc]]></openid>\n" +
                "  <trade_type><![CDATA[JSAPI]]></trade_type>\n" +
                "  <coupon_fee><![CDATA[3]]></coupon_fee>\n" +
                "  <cash_fee_type><![CDATA[CNY]]></cash_fee_type>\n" +
                "  <nonce_str><![CDATA[1522461979748]]></nonce_str>\n" +
                "  <time_end><![CDATA[20180331100621]]></time_end>\n" +
                "  <err_code_des><![CDATA[SUCCESS]]></err_code_des>\n" +
                "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "  <mch_id><![CDATA[1497713532]]></mch_id>\n" +
                "  <settlement_total_fee><![CDATA[100]]></settlement_total_fee>\n" +
                "  <sign><![CDATA[E5180F0F5463FC9915BDDB4960821743]]></sign>\n" +
                "  <cash_fee><![CDATA[99]]></cash_fee>\n" +
                "  <coupon_id_1><![CDATA[67890]]></coupon_id_1>\n" +
                "  <coupon_type_0><![CDATA[CASH]]></coupon_type_0>\n" +
                "  <coupon_id_0><![CDATA[123456]]></coupon_id_0>\n" +
                "  <coupon_fee_0><![CDATA[1]]></coupon_fee_0>\n" +
                "  <coupon_fee_1><![CDATA[2]]></coupon_fee_1>\n" +
                "  <is_subscribe><![CDATA[Y]]></is_subscribe>\n" +
                "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                "  <fee_type><![CDATA[CNY]]></fee_type>\n" +
                "  <bank_type><![CDATA[CMC]]></bank_type>\n" +
                "  <attach><![CDATA[sandbox_attach]]></attach>\n" +
                "  <device_info><![CDATA[sandbox]]></device_info>\n" +
                "  <out_trade_no><![CDATA[979902681936035840]]></out_trade_no>\n" +
                "  <coupon_type_1><![CDATA[NO_CASH]]></coupon_type_1>\n" +
                "  <result_code><![CDATA[SUCCESS]]></result_code>\n" +
                "  <total_fee><![CDATA[102]]></total_fee>\n" +
                "  <appid><![CDATA[wxe184298fc71118e4]]></appid>\n" +
                "  <coupon_count><![CDATA[2]]></coupon_count>\n" +
                "  <transaction_id><![CDATA[100539073720180331100621748318]]></transaction_id>\n" +
                "  <err_code><![CDATA[SUCCESS]]></err_code>\n" +
                "</xml>";

        /*
        校验签名时，需要将配置文件mchKey设置为967e93eb60ccdc8860108ed763852c75，useSandboxEnv设置为false
         */
        WxPayOrderNotifyResult wxPayOrderNotifyResult = wxPayService.parseOrderNotifyResult(xml);
        Assert.assertNotNull(wxPayOrderNotifyResult);
    }
}
