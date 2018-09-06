package com.delllogistics.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 项目相关配置
 *
 * @author xzm
 */
@Configuration
@ConfigurationProperties(prefix = RestProperties.REST_PREFIX)
@Setter
@Getter
public class RestProperties {

    public static final String REST_PREFIX = "rest";

    private boolean signOpen = true;
    private boolean ssoOpen = true;

}
