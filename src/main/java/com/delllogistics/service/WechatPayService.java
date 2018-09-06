package com.delllogistics.service;

import com.delllogistics.entity.order.OrderMain;
import com.delllogistics.entity.sys.WXPayInfo;
import com.delllogistics.entity.sys.WechatConfig;
import com.delllogistics.repository.sys.WXPayInfoRepository;
import com.delllogistics.repository.sys.WechatConfigRepository;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wechat Pay.<br/>
 * User: jiajie<br/>
 * Date: 15/02/2018<br/>
 * Time: 2:52 PM<br/>
 */
@Service
public class WechatPayService {

    private final WXPayInfoRepository wxPayInfoRepository;

    private final WechatConfigRepository wechatConfigRepository;

    @Autowired
    public WechatPayService(WechatConfigRepository wechatConfigRepository, WXPayInfoRepository wxPayInfoRepository) {
        this.wechatConfigRepository = wechatConfigRepository;
        this.wxPayInfoRepository = wxPayInfoRepository;
    }

    public List<WXPayInfo> findByOrder(OrderMain order) {
        Specification<WXPayInfo> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("order"), order));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return wxPayInfoRepository.findAll(specification);
    }

    public void saveWXPayInfo(WXPayInfo info) {
        wxPayInfoRepository.save(info);
    }

    private Map<String, String> buildWXPay(OrderMain order, String appId) {
        WXPayConfigImpl instance;
        try {
            WechatConfig config = wechatConfigRepository.findByAppId(appId);
            instance = WXPayConfigImpl.getInstance(config);

            HashMap<String, String> data = new HashMap<>();
            data.put("body", "果麦课程报名");
            data.put("out_trade_no", order.getCode().toString());
            data.put("device_info", "");
            data.put("fee_type", "CNY");
            data.put("total_fee", String.valueOf(order.getOrderAmount().multiply(new BigDecimal(100)).setScale(0)));
            data.put("spbill_create_ip", StringUtils.isEmpty(order.getUser().getWechatUser().getSpbillCreateIp()) ? "0.0.0.0" : order.getUser().getWechatUser().getSpbillCreateIp());
            data.put("notify_url", config.getNotifyUrl());
            data.put("trade_type", "JSAPI");
            data.put("openid", order.getUser().getWechatUser().getOpenId());
            Map<String, String> respMap = new WXPay(instance).unifiedOrder(data);
            Map<String, String> pp = new HashMap<>();
            pp.put("appId", appId);
            pp.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
            pp.put("nonceStr", WXPayUtil.generateNonceStr());
            pp.put("package", "prepay_id=" + respMap.get("prepay_id"));
            pp.put("signType", WXPayConstants.SignType.MD5.toString());
            pp.put("sign", WXPayUtil.generateSignature(pp, config.getPaySecret(), WXPayConstants.SignType.MD5));

            saveWXPayInfo(WXPayInfo.addInitPayInfo(null, data, pp, order));
            return pp;
        } catch (Exception e) {
            throw new RuntimeException("微信支付异常！", e);
        }

    }
}
