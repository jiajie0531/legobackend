package com.delllogistics.spring.annotation;

import java.lang.annotation.*;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: jiajie<br/>
 * Date: 28/01/2018<br/>
 * Time: 2:39 PM<br/>
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    String value() default "";

    int[] paramIndexs() default {};
}
