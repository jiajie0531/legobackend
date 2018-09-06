package com.delllogistics.service;

import com.delllogistics.entity.sys.WechatConfig;
import com.delllogistics.repository.sys.WechatConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**微信配置
 * Created by xzm on 2018-1-12.
 */
@Service
public class WechatConfigService {

    private final WechatConfigRepository wechatConfigRepository;

    @Autowired
    public WechatConfigService(WechatConfigRepository wechatConfigRepository) {
        this.wechatConfigRepository = wechatConfigRepository;
    }

    public WechatConfig getWechatConfigById(String appId){
        return wechatConfigRepository.findByAppId(appId);
    }
}
