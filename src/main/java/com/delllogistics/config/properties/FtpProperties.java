package com.delllogistics.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Ftp上传配置
 */
@Configuration
@ConfigurationProperties(prefix = FtpProperties.PREFIX)
@Getter
@Setter
public class FtpProperties {

    public static final String PREFIX="storage.ftp.server";

    private String hostname;
    private String username;
    private String password;
    private String loadDir;
}
