package com.delllogistics.config;

import com.delllogistics.config.properties.FtpProperties;
import com.delllogistics.config.properties.QiniuUplodProperties;
import com.delllogistics.storage.StorageInterface;
import com.delllogistics.storage.impl.FTPStorageImpl;
import com.delllogistics.storage.impl.QiniuStorageImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Value("${storage.enable}")
    private String enable;

    private final FtpProperties ftpProperties;

    private final QiniuUplodProperties qiniuUplodProperties;

    @Autowired
    public StorageConfig(FtpProperties ftpProperties, QiniuUplodProperties qiniuUplodProperties) {
        this.ftpProperties = ftpProperties;
        this.qiniuUplodProperties = qiniuUplodProperties;
    }


    @Bean
    public StorageInterface initStorage(){
        if("qiniu".equals(enable)){
            return new QiniuStorageImpl(qiniuUplodProperties);
        }else if("ftp".equals(enable)){
            return new FTPStorageImpl(ftpProperties);
        }
        return new QiniuStorageImpl(qiniuUplodProperties);

    }
}
