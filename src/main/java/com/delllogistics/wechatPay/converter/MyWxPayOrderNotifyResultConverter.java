package com.delllogistics.wechatPay.converter;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyCoupon;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.converter.WxPayOrderNotifyResultConverter;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/** 微信支付返回结果XML解析
 * Created by xzm on 2018-4-2.
 */
public class MyWxPayOrderNotifyResultConverter extends WxPayOrderNotifyResultConverter{

    public MyWxPayOrderNotifyResultConverter(Mapper mapper, ReflectionProvider reflectionProvider) {
        super(mapper, reflectionProvider);
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        WxPayOrderNotifyResult obj = new WxPayOrderNotifyResult();

        List<Field> fields = new ArrayList<>(Arrays.asList(obj.getClass().getDeclaredFields()));
        fields.addAll(Arrays.asList(obj.getClass().getSuperclass().getDeclaredFields()));
        Map<String, Field> fieldMap = getFieldMap(fields);

        List<WxPayOrderNotifyCoupon> coupons = new ArrayList<>(10);
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            if (fieldMap.containsKey(reader.getNodeName())) {
                Field field = fieldMap.get(reader.getNodeName());
                setFieldValue(context, obj, field);
            } else if (StringUtils.startsWith(reader.getNodeName(), "coupon_id_")) {
                String id = (String) context.convertAnother(obj, String.class);
                getIndex(coupons, reader.getNodeName()).setCouponId(id);
            } else if (StringUtils.startsWith(reader.getNodeName(), "coupon_type_")) {
                String type = (String) context.convertAnother(obj, String.class);
                getIndex(coupons, reader.getNodeName()).setCouponType(type);
            } else if (StringUtils.startsWith(reader.getNodeName(), "coupon_fee_")) {
                Integer fee = (Integer) context.convertAnother(obj, Integer.class);
                getIndex(coupons, reader.getNodeName()).setCouponFee(fee);
            }
            reader.moveUp();
        }

        obj.setCouponList(coupons);
        return obj;
    }

    private Map<String, Field> getFieldMap(List<Field> fields) {
        Map<String, Field> fieldMap = Maps.uniqueIndex(fields, new Function<Field, String>() {
            @Override
            public String apply(Field field) {
                if (field.isAnnotationPresent(XStreamAlias.class)) {
                    return field.getAnnotation(XStreamAlias.class).value();
                }
                return field.getName();
            }
        });
        return fieldMap;
    }

    private void setFieldValue(UnmarshallingContext context, WxPayOrderNotifyResult obj, Field field) {
        Object val = context.convertAnother(obj, field.getType());
        try {
            if (val != null) {
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), obj.getClass());
                pd.getWriteMethod().invoke(obj, val);
            }
        } catch (Exception e) {
        }
    }

    private static WxPayOrderNotifyCoupon getIndex(List<WxPayOrderNotifyCoupon> coupons, String nodeName) {
        Integer index = Integer.valueOf(StringUtils.substring(nodeName, nodeName.lastIndexOf("_") + 1));
        putNullToList(coupons,index);
        if (index >= coupons.size() || coupons.get(index) == null) {
            coupons.add(index, new WxPayOrderNotifyCoupon());
        }
        return coupons.get(index);
    }

    /**
     * 解决xml中dom节点无序导致的IndexOutOfBoundsException
     * @param coupons 节点数组
     * @param index 索引
     */
    private static void putNullToList(List<WxPayOrderNotifyCoupon> coupons ,Integer index){
        int size = coupons.size();
        for (int i = size; i < index; i++) {
            coupons.add(i,new WxPayOrderNotifyCoupon());
        }
    }

    public static void main(String[] args) {
        List<WxPayOrderNotifyCoupon> list=new ArrayList<>();
        WxPayOrderNotifyCoupon index = getIndex(list, "ab_1");
        System.out.println(index);
        System.out.println(list);
    }
}
