package com.delllogistics.dto.finance;

import com.delllogistics.dto.BaseSearchModel;
import com.delllogistics.entity.enums.PayChannel;
import com.delllogistics.entity.enums.PayStatus;
import com.delllogistics.entity.enums.PayType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Getter
@Setter
public class FinanceReceivableSearch extends BaseSearchModel {


    /**
     * 收款单号
     */
    private String financeReceivableCode;
    /**
     * 订单号
     */
    private String orderMainCode;

    /**
     * 支付方式
     */
    @Enumerated(EnumType.STRING)
    private PayChannel payChannel;
    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    private PayStatus payStatus;

    /**
     * 支付方式
     */
    @Enumerated(EnumType.STRING)
    private PayType payType;

    /**
     * 交易代码（可关联第三方平台交易id）
     */
    private String transactionCode;


}
