package com.delllogistics.util;

import java.util.List;
import java.util.Set;

/** 集合工具类
 * Created by xzm on 2017/10/19.
 */
public class ArrayUtil {

    public static boolean isEmpty(List<?> list){
        return list==null?true:list.isEmpty();
    }

    public static boolean isEmpty(Set<?> set){
        return set==null?true:set.isEmpty();
    }



    public static String addQuotes(String[] array){
        StringBuilder rst = new StringBuilder();
        for (int i = 0; i < array.length; i++)
        {
            if(i<array.length-1){
                rst.append("'").append(array[i]).append("',");
            }else{
                rst.append("'").append(array[i]).append("'");
            }


        }
        return rst.toString();
    }



}
