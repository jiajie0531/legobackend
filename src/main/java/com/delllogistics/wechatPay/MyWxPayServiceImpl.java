package com.delllogistics.wechatPay;

import com.delllogistics.wechatPay.utils.WxPayOrderNotifyResultUtil;
import com.delllogistics.wechatPay.utils.WxPayRefundNotifyResultUtil;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;

/** 自定义微信支付XML解析
 * Created by xzm on 2018-4-2.
 */
public class MyWxPayServiceImpl extends WxPayServiceImpl{

    @Override
    public WxPayOrderNotifyResult parseOrderNotifyResult(String xmlData) throws WxPayException {
        try {
            log.debug("微信支付异步通知请求参数：{}", xmlData);
            WxPayOrderNotifyResult result = WxPayOrderNotifyResultUtil.fromXML(xmlData);
            log.debug("微信支付异步通知请求解析后的对象：{}", result);
            result.checkResult(this, this.getConfig().getSignType(), false);
            return result;
        } catch (WxPayException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new WxPayException("发生异常，" + e.getMessage(), e);
        }
    }

    @Override
    public WxPayRefundNotifyResult parseRefundNotifyResult(String xmlData) throws WxPayException {
        try {
            log.debug("微信支付退款异步通知参数：{}", xmlData);
            WxPayRefundNotifyResult result = WxPayRefundNotifyResultUtil.fromXML(xmlData, this.getConfig().getMchKey());
            log.debug("微信支付退款异步通知解析后的对象：{}", result);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new WxPayException("发生异常，" + e.getMessage(), e);
        }
    }


}
