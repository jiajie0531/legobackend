package com.delllogistics.entity.enums;

import com.delllogistics.util.EnumItem;
import com.delllogistics.util.IEnumList;

/**
 *  运费类型
 * Created by calvin  2018/4/28
 */
public enum LogisticsFreightType  implements IEnumList {

    CUSTOM( 10101,"CUSTOM", "自定义"),
    FREE(10102,"FREE",  "包邮");


    private EnumItem item=new EnumItem();

    LogisticsFreightType(int id,String enName,String cnName) {
        item.setId(id);
        item.setEnName(enName);
        item.setCnName(cnName);
    }

    @Override
    public EnumItem getEnumItem() {
        return item;
    }
}
