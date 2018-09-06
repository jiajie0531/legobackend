package com.delllogistics.entity.enums;

import com.delllogistics.util.EnumItem;
import com.delllogistics.util.IEnumList;

/**
 *  运送方式
 * Created by calvin  2018/4/28
 */
public enum LogisticsDeliveryType implements IEnumList {

    EXPRESS(10401, "EXPRESS", "快递"),
    EMPLOYEE(10402,"EMPLOYEE",  "师傅上门"),
    PICK_UP(10403,"PICK_UP",  "自提");

    private EnumItem item=new EnumItem();

    LogisticsDeliveryType(int id, String enName, String cnName) {
        item.setId(id);
        item.setEnName(enName);
        item.setCnName(cnName);
    }

    @Override
    public EnumItem getEnumItem() {
        return item;
    }
}
