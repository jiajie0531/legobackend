package com.delllogistics.config;

import com.delllogistics.config.properties.RestProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Created by jiajie on 15/04/2017.
 */
@Configuration
@ConfigurationProperties(prefix = RestProperties.REST_PREFIX)
public class CorsConfig {
    public static final String REST_PREFIX = "cors";

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // 1
        /*
        corsConfiguration.addAllowedOrigin("http://localhost:3000"); // 1
        corsConfiguration.addAllowedOrigin("http://localhost:5000"); // 1
        corsConfiguration.addAllowedOrigin("http://localhost:9001"); // 1
        */
        corsConfiguration.addAllowedHeader("*"); // 2
        corsConfiguration.addAllowedMethod("*"); // 3
        //corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig()); // 4
        return new CorsFilter(source);
    }
}

