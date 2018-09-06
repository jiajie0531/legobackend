package com.delllogistics.spring.annotation;

import org.springframework.stereotype.Controller;

import java.lang.annotation.*;

/** 自定义RestController,用于过滤返回对象
 * Created by xzm on 2017-11-21.
 *  @see JsonConvert
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
@JsonConvert
public @interface RestJsonController {
}
