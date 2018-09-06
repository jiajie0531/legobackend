package com.delllogistics.config;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.delllogistics.converter.WithSignMessageConverter;
import com.delllogistics.spring.method.CurrentUserMethodResolver;
import com.delllogistics.spring.method.JsonReturnHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    private final FastJsonConfig fastJsonConfig;

    private final WithSignMessageConverter fastJsonHttpMessageConverter;

    @Autowired
    public WebMvcConfig(FastJsonConfig fastJsonConfig, WithSignMessageConverter fastJsonHttpMessageConverter) {
        this.fastJsonConfig = fastJsonConfig;
        this.fastJsonHttpMessageConverter = fastJsonHttpMessageConverter;
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseRegisteredSuffixPatternMatch(true);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(initFastJsonConverter());
        /*
		 * 处理返回String中文乱码问题
		 */
        StringHttpMessageConverter stringConverter=new StringHttpMessageConverter(Charset.forName("UTF-8"));
        converters.add(stringConverter);
    }
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new CurrentUserMethodResolver());
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        returnValueHandlers.add(new JsonReturnHandler(fastJsonConfig));
    }





    private FastJsonHttpMessageConverter initFastJsonConverter(){
        // 解决中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        return fastJsonHttpMessageConverter;
    }

}
