package com.delllogistics.dto.logistics;

import com.delllogistics.dto.BaseSearchModel;
import com.delllogistics.entity.enums.LogisticsDeliveryTime;
import com.delllogistics.entity.enums.LogisticsFreightType;
import com.delllogistics.entity.enums.LogisticsPriceType;
import com.delllogistics.entity.enums.LogisticsDeliveryType;
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
public class LogisticsTemplateSearch extends BaseSearchModel {
    /**
     * 物流模版名称
     */
    private String name;

    /**
     * 模板Id
     */
    private Long templateId;

    /**
     * 发货时间
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32,nullable = false)
    private LogisticsDeliveryTime deliveryTime;

    /**
     * 运送方式
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32,nullable = false)
    private LogisticsDeliveryType shippingType;


    /**
     * 运费类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32,nullable = false)
    private LogisticsFreightType freightType;

    /**
     * 计费类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32,nullable = false)
    private LogisticsPriceType priceType;

    /**
     * 宝贝地址
     */
    private Long areaId;

    /**
     * 是否可用
     */
    private boolean isUsed;
    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }
    public boolean getIsUsed() {
        return isUsed;
    }
}