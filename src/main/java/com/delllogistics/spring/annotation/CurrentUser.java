package com.delllogistics.spring.annotation;

import java.lang.annotation.*;

/** 自定义用户注解
 * Created by Administrator on 2017-11-15.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {

    String value() default "";
}
