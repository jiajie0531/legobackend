package com.delllogistics.entity.sys;

import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.order.OrderMain;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.Map;
import java.util.Objects;

@SQLDelete(sql = "update WXPayInfo set is_deleted=1,update_time=now() where id=? and version_=?")
@Entity
@Getter
@Setter
public class WXPayInfo extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    private OrderMain order;
    /**
     * 初始化请求参数
     */
    private String initBody;
    private String initOutTradeNo;
    private String initDeviceInfo;
    private String initFeeType;
    private String initTotalFee;
    private String initSpbillCreateIp;
    private String initNotifyUrl;
    private String initTradeType;
    private String initOpenid;

    /**
     * 支付提交参数
     */
    private String reqAppId;
    private String reqTimeStamp;
    private String reqNonceStr;
    private String reqPackage;
    private String reqSignType;
    private String reqSign;

    /**
     * 支付结果数据
     */
    @Column(length = 1000)
    private String payResult;

    public WXPayInfo() {
    }

    public static WXPayInfo addInitPayInfo(WXPayInfo info, Map<String, String> initData, Map<String, String> prepareData, OrderMain order) {
        if(Objects.isNull(info)) {
            info = new WXPayInfo();
        }

        info.setOrder(order);
        if(!CollectionUtils.isEmpty(initData)) {
            info.setInitBody(initData.get("body"));
            info.setInitDeviceInfo(initData.get("device_info"));
            info.setInitFeeType(initData.get("fee_type"));
            info.setInitNotifyUrl(initData.get("notify_url"));
            info.setInitOpenid(initData.get("openid"));
            info.setInitOutTradeNo(initData.get("out_trade_no"));
            info.setInitSpbillCreateIp(initData.get("spbill_create_ip"));
            info.setInitTotalFee(initData.get("total_fee"));
            info.setInitTradeType(initData.get("trade_type"));
        }

        if(!CollectionUtils.isEmpty(prepareData)) {
            info.setReqAppId(prepareData.get("appId"));
            info.setReqTimeStamp(prepareData.get("timeStamp"));
            info.setReqNonceStr(prepareData.get("nonceStr"));
            info.setReqPackage(prepareData.get("package"));
            info.setReqSignType(prepareData.get("signType"));
            info.setReqSign(prepareData.get("sign"));
        }
        return info;
    }


}
