package com.delllogistics.storage.impl;

import com.alibaba.fastjson.JSON;
import com.delllogistics.config.properties.QiniuUplodProperties;
import com.delllogistics.storage.StorageInterface;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class QiniuStorageImpl implements StorageInterface {

    private Logger logger= LoggerFactory.getLogger(getClass());

    private final QiniuUplodProperties qiniuUplodProperties;

    public QiniuStorageImpl(QiniuUplodProperties qiniuUplodProperties) {
        this.qiniuUplodProperties = qiniuUplodProperties;
    }

    @Override
    public boolean uploadFile(String filename, byte[] uploadBytes) {
        UploadManager uploadManager = new UploadManager(initConfiguration());
        try {
            Response response = uploadManager.put(uploadBytes, filename, getUpToken());
            logger.info(response.bodyString());
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            logger.info("上传 {} hash值：{}",putRet.key,putRet.hash);
            return true;
        } catch (QiniuException e) {
            logger.error("七牛云存储上传文件失败",e);
        }
        return false;
    }

    @Override
    public boolean uploadFile(String filename, InputStream input) {
        return true;
    }

    @Override
    public String getStorageDomain() {
        return qiniuUplodProperties.getDomain();
    }


    private String getUpToken(){
        Auth auth = Auth.create(qiniuUplodProperties.getAccessKey(), qiniuUplodProperties.getSecretKey());
        return auth.uploadToken(qiniuUplodProperties.getBucket());
    }

    private Configuration initConfiguration(){

        Configuration cfg;
        switch (qiniuUplodProperties.getZone()){
            case zone0:
                cfg= new Configuration(Zone.zone0());
                break;
            case zone1:
                cfg= new Configuration(Zone.zone1());
                break;
            case zone2:
                cfg= new Configuration(Zone.zone2());
                break;
            case zoneNa0:
                cfg= new Configuration(Zone.zoneNa0());
                break;
            default:
                cfg= new Configuration(Zone.zone0());
                break;
        }
        return cfg;

    }
}
