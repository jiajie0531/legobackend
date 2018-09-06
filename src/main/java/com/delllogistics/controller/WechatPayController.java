package com.delllogistics.controller;

import com.delllogistics.entity.order.OrderMain;
import com.delllogistics.entity.sys.WXPayInfo;
import com.delllogistics.entity.enums.OrderMainStatus;
import com.delllogistics.service.order.OrderMainService;
import com.delllogistics.service.WechatPayService;
import com.github.wxpay.sdk.WXPayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

/**
 * Wechat Pay.<br/>
 * User: jiajie<br/>
 * Date: 15/02/2018<br/>
 * Time: 3:14 PM<br/>
 */
@Controller
@RequestMapping("/wechatpay")
public class WechatPayController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OrderMainService orderMainService;

    private final WechatPayService wechatPayService;

    @Autowired
    public WechatPayController(OrderMainService orderMainService, WechatPayService wechatPayService) {
        this.orderMainService = orderMainService;
        this.wechatPayService = wechatPayService;
    }

    @PostMapping("/wxNotify")
    public void notifyOrderByWX(@RequestBody String requestBody, HttpServletResponse response) throws IOException {
        final String SUCCESS = "SUCCESS", FAIL = "FAIL", OK = "OK", RETURN_CODE = "return_code", RESULT_CODE = "result_code";
        final String OUT_TRADENO_KEY = "out_trade_no";

        PrintWriter printWriter = response.getWriter();
        try {
            Map<String, String> respMap = WXPayUtil.xmlToMap(requestBody);

            String returnCode = String.valueOf(respMap.get(RETURN_CODE));
            String resultCode = String.valueOf(respMap.get(RESULT_CODE));

            String outTradeNo = String.valueOf(respMap.get(OUT_TRADENO_KEY));
            OrderMain order = orderMainService.findOrderByCode(outTradeNo);
            if (SUCCESS.equals(returnCode) && SUCCESS.equals(resultCode)) {
                if (!Objects.isNull(order)) {
                    //待支付及金额正确
                    if (OrderMainStatus.WAIT_TO_PAY.equals(order.getStatus())
                            && order.getOrderAmount().multiply(new BigDecimal(100)).compareTo(new BigDecimal(respMap.get("total_fee"))) == 0) {
                        order.setStatus(OrderMainStatus.PAID);
                        orderMainService.saveOrder(order);
                    }
                    printWriter.write(wxResponse(SUCCESS, OK));
                    logger.info("微信支付成功！", requestBody);
                    try {
                        WXPayInfo info = wechatPayService.findByOrder(order).get(0);
                        info.setPayResult(requestBody);
                        wechatPayService.saveWXPayInfo(info);
                    } catch (Exception e) {
                        logger.info("保存支付结果异常！", e);
                    }
                } else {
                    printWriter.write(wxResponse(FAIL, "未找到对应订单"));
                    logger.error("微信支付通知支付成功，但是未找到相应订单", requestBody);
                }
            } else {
                logger.error("支付失败！", requestBody);
            }
        } catch (Exception e) {
            printWriter.write(wxResponse(FAIL, "解析参数异常！"));
        }
        printWriter.flush();
    }

    private String wxResponse(String code, String msg) {
        StringBuffer reqSuccess = new StringBuffer("<xml>");
        reqSuccess.append("<return_code><![CDATA[" + code + "]]></return_code>");
        reqSuccess.append("<return_msg><![CDATA[" + msg + "]]></return_msg>");
        reqSuccess.append("</xml>");
        return reqSuccess.toString();
    }
}
