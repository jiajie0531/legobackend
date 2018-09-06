package com.delllogistics.util;

import java.util.UUID;

/**
 * Created by xzm on 2017/6/5.
 */
public class TokenUtils {

    public static String WXPREV="wx-";

    /**
     * 生成token，此处为UUID
     * @return
     */
    public static String createToken(){
        String s = UUID.randomUUID().toString();
        return s.replace("-", "");
    }

    public static String createWechatToken(){
        return WXPREV+createToken();
    }
}
