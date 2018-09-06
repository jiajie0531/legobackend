package com.delllogistics.spring.annotation;

import java.lang.annotation.*;

/**多级对象字段过滤
 * (@Code @NestConvert(type=Role.class,includes = {"id"}))
 * @see JsonConvert
 * Created by xzm on 2017-11-20.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NestConvert {
    Class<?> type();
    String[] includes() default "";
    String[] excludes() default "";
}
