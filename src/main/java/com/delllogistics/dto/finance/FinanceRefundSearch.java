package com.delllogistics.dto.finance;

import com.delllogistics.dto.BaseSearchModel;
import com.delllogistics.entity.enums.PayChannel;
import com.delllogistics.entity.enums.PayRefundChannel;
import com.delllogistics.entity.enums.PayStatus;
import com.delllogistics.entity.enums.PayType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Getter
@Setter
public class FinanceRefundSearch extends BaseSearchModel {


    /**
     * 退单ID.退单编号
     */
    private String financeRefundCode;
    /**
     * 订单ID.订单编号
     */
    private String orderMainCode;

    /**
     * 退款方式
     */
    @Enumerated(EnumType.STRING)
    private PayRefundChannel payRefundChannel;

    /**
     * 交易类型
     */
    @Enumerated(EnumType.STRING)
    private PayType payType;

    /**
     * 交易渠道
     */
    @Enumerated(EnumType.STRING)
    private PayChannel payChannel;
    /**
     * 交易代码（可关联第三方平台交易id）
     */
    private String transactionCode;

    /**
     * 交易状态
     */
    @Enumerated(EnumType.STRING)
    private PayStatus payStatus;



}
