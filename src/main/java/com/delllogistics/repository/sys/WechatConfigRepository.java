package com.delllogistics.repository.sys;

import com.delllogistics.entity.sys.WechatConfig;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * Created by jiajie on 11/04/2017.
 */
@Transactional
public interface WechatConfigRepository extends CrudRepository<WechatConfig, Long> {

    WechatConfig findByAppId(String appId);
}
