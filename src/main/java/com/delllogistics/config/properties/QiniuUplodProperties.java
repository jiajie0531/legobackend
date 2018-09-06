package com.delllogistics.config.properties;

import com.delllogistics.entity.enums.QiniuZoneEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = QiniuUplodProperties.PREFIX)
@Getter
@Setter
public class QiniuUplodProperties {

    public static final String PREFIX="storage.qiniu";

    private String accessKey;

    private String secretKey;

    private String bucket;

    private QiniuZoneEnum zone;

    private String domain;
}
