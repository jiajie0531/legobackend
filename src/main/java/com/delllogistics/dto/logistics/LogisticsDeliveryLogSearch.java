package com.delllogistics.dto.logistics;

import com.delllogistics.dto.BaseSearchModel;
import com.delllogistics.entity.enums.LogisticsDeliveryTime;
import com.delllogistics.entity.enums.LogisticsDeliveryType;
import com.delllogistics.entity.enums.LogisticsFreightType;
import com.delllogistics.entity.enums.LogisticsPriceType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 *  物流模版搜索条件
 * Created by calvin  2018/1/17
 */
@Setter
@Getter
public class LogisticsDeliveryLogSearch extends BaseSearchModel {

    /**
     * 订单id
     */
    private Long orderMainId;

    /**
     * 退单id
     */
    private Long orderRefundId;





}