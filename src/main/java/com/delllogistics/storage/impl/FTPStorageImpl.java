package com.delllogistics.storage.impl;

import com.delllogistics.config.properties.FtpProperties;
import com.delllogistics.storage.StorageInterface;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FTPStorageImpl implements StorageInterface{

    private Logger logger= LoggerFactory.getLogger(getClass());

    private final FtpProperties ftpProperties;

    public FTPStorageImpl(FtpProperties ftpProperties) {
        this.ftpProperties = ftpProperties;
    }

    @Override
    public boolean uploadFile(String filename, byte[] bytes) {
        InputStream input=new ByteArrayInputStream(bytes);
        return uploadFile(filename,input);
    }

    @Override
    public boolean uploadFile(String filename, InputStream input) {
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(ftpProperties.getHostname());//连接FTP服务器
            //如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.login(ftpProperties.getUsername(), ftpProperties.getPassword());//登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return false;
            }
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            //ftp.changeWorkingDirectory(path);
            ftp.storeFile(filename, input);
            input.close();
            ftp.logout();
            return true;
        } catch (IOException e) {
            logger.error("FTP上传IO异常",e);
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    logger.error("FTP关闭IO异常",ioe);
                }
            }
        }
        return false;
    }

    @Override
    public String getStorageDomain() {
        return ftpProperties.getLoadDir();
    }
}
