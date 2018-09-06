package com.delllogistics.util;

import com.delllogistics.entity.enums.LogisticsDeliveryTime;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumHelper {

    /**
     * 获取枚举集合
     * @param value Enum
     * @param <T> 对象
     * @return List<EnumItem>
     */
    public static <T extends IEnumList> List<EnumItem> getEnumList(Class<? extends IEnumList> value) {
        T[] q=(T[]) value.getEnumConstants();
        List<EnumItem> v= Arrays.stream(q).map(T::getEnumItem).collect(Collectors.toList());
        return v;
    }



    public static  IEnumList  codeOf(Class<? extends IEnumList>  enumClass, int code) {
        IEnumList[] enumConstants = enumClass.getEnumConstants();
        for (IEnumList e : enumConstants) {
            if (e.getEnumItem().getId() == code)
                return e;
        }
        return null;
    }
    /**
     * 根据枚举类型和key获取信息
     * @param value Enum
     * @param key Enum key
     * @param <T> 对象
     * @return EnumItem
     */
    public static <T extends IEnumList> EnumItem getEnumItem(Class<? extends IEnumList> value,int key) {
        T[] q=(T[]) value.getEnumConstants();
        T v=(Arrays.stream(q)).filter(x-> x.getEnumItem().getId()==key).findFirst().get();
        if(v==null)
            return null;
        EnumItem s= v.getEnumItem();
        return s;
    }
    public static void main(String[] args) {
        //直接通过枚举值来获取信息
        //获取Sex枚举信息
        List<EnumItem> s = EnumHelper.getEnumList(LogisticsDeliveryTime.class);
        //System.out.println(s.get(0).getCnName());

        //根据枚举类型 和 key 获取此枚举信息
        IEnumList v = EnumHelper.codeOf(LogisticsDeliveryTime.class,10201);
        if(v!=null){
            System.out.println(v.getEnumItem().getCnName());
        }


    }
}
