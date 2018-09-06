package com.delllogistics.util;

import java.math.BigDecimal;

/** 金额工具类
 * Created by xzm on 2018-3-30.
 */
public class MoneyUtils {

    /**
     * 元转换为分
     * @param yuan 元
     * @return 分
     */
    public static Integer yuanToFen(BigDecimal yuan){
        return yuan.setScale(2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).intValue();
    }

    public static BigDecimal fenToYuan(Integer fen){
        return BigDecimal.valueOf(fen).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP);
    }


}
