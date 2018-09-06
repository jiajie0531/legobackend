package com.delllogistics.config;

import com.alibaba.fastjson.serializer.*;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.lang.reflect.Type;

/**fastJson配置
 * Created by xzm on 2017-11-21.
 */
@Configuration
public class JsonConfig {

    @Bean
    public FastJsonConfig initFastJsonConfig(){
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat,
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat
        );
        fastJsonConfig.getSerializeConfig().put(Long.class,new ToStringSerializer());
        return fastJsonConfig;
    }
}
