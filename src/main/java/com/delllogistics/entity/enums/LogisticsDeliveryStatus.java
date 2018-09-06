package com.delllogistics.entity.enums;

import com.delllogistics.util.EnumItem;
import com.delllogistics.util.IEnumList;

/**
 * 物流发送状态
 * Created by calvin  2018/4/28
 */
public enum LogisticsDeliveryStatus implements IEnumList {

    BUSINESS_SHIPPED(10501, "BUSINESS_SHIPPED", "商家已发货"),
    BUYER_DELIVERY(10502, "BUYER_DELIVERY", "客户已确认");


    private EnumItem item = new EnumItem();

    LogisticsDeliveryStatus(int id, String enName, String cnName) {
        item.setId(id);
        item.setEnName(enName);
        item.setCnName(cnName);
    }

    @Override
    public EnumItem getEnumItem() {
        return item;
    }
}
