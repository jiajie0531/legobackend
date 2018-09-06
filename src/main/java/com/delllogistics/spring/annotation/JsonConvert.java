package com.delllogistics.spring.annotation;

import com.delllogistics.spring.DTO;

import java.lang.annotation.*;

/** 处理返回对象
 * 转换DTO时 (@Code )
 * 过滤返回对象 (@Code @JsonConvert(type = User.class,
 includes = {"id","username","createTime","roles","companies"}
 )
 * Created by xzm on 2017-11-20.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonConvert {
    Class<?> type() default DTO.class; //过滤类型，默认为DTO时不过滤
    String[] includes() default {};//指定属性
    String[] excludes() default {};//过滤属性
    NestConvert[] nest() default {};//指定嵌套对象属性

    Class<?> dto() default DTO.class; //转换对象，不为默认值时进行转换，以上属性无效
}
