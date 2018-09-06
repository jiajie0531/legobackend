package com.delllogistics.entity.enums;

import com.delllogistics.util.EnumItem;
import com.delllogistics.util.IEnumList;

import javax.persistence.AttributeConverter;

/**
 *  发货时间
 * Created by calvin  2018/4/28
 */
public enum LogisticsDeliveryTime  implements IEnumList {

    FOUR_HOUR(10201, "FOUR_HOUR", "4小时内"),
    EIGHT_HOUR( 10202, "EIGHT_HOUR","8小时内"),
    TWELVE_HOUR(10203, "TWELVE_HOUR", "12小时内"),
    SIXTEEN_HOUR(10204, "SIXTEEN_HOUR", "16小时内"),
    TWENTY_HOUR(10205,"TWENTY_HOUR",  "20小时内"),
    TWENTY_FOUR_HOUR( 10206,"TWENTY_FOUR_HOUR", "24小时内"),
    TWO_DAY( 10207, "TWO_DAY","2天内"),
    THREE_DAY(10208, "THREE_DAY", "3天内"),
    FIVE_DAY(10209,"FIVE_DAY",  "5天内"),
    SEVEN_DAY(10211,"SEVEN_DAY",  "7天内"),
    EIGHT_DAY( 10212, "EIGHT_DAY","8天内");




    private EnumItem item=new EnumItem();

    LogisticsDeliveryTime(int id,String enName,String cnName) {
        item.setId(id);
        item.setEnName(enName);
        item.setCnName(cnName);
    }

    @Override
    public EnumItem getEnumItem() {
        return item;
    }


    public static class Convert implements AttributeConverter<LogisticsDeliveryTime, Integer> {
        @Override
        public Integer convertToDatabaseColumn(LogisticsDeliveryTime attribute) {
            return attribute == null ? null : attribute.item.getId();
        }

        @Override
        public LogisticsDeliveryTime convertToEntityAttribute(Integer dbData) {
            for (LogisticsDeliveryTime type : LogisticsDeliveryTime.values()) { //将数字转换为描述
                if (dbData.equals(type.item.getId())) {
                    return type;
                }
            }
            throw new RuntimeException("Unknown database value: " + dbData);
        }
    }
}
