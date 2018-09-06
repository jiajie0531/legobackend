package com.delllogistics.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ModelMapper Config.<br/>
 * User: jiajie<br/>
 * Date: 28/11/2017<br/>
 * Time: 7:52 AM<br/>
 */
@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
