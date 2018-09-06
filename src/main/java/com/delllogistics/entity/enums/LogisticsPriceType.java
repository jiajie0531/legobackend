package com.delllogistics.entity.enums;

import com.delllogistics.util.EnumItem;
import com.delllogistics.util.IEnumList;

/**
 *  计费类型
 * Created by calvin  2018/4/28
 */
public enum LogisticsPriceType implements IEnumList {

    PIECE( 10301,"PIECE", "按件"),
    WEIGHT(10302, "WEIGHT", "按重量"),
    VOLUME(10303, "VOLUME", "按体积");

    private EnumItem item=new EnumItem();

    LogisticsPriceType(int id,String enName,String cnName) {
        item.setId(id);
        item.setEnName(enName);
        item.setCnName(cnName);
    }

    @Override
    public EnumItem getEnumItem() {
        return item;
    }
}
