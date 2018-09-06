package com.delllogistics.repository.sys;

import com.delllogistics.entity.sys.WechatToken;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * Created by jiajie on 15/04/2017.
 */
@Transactional
public interface WechatTokenRepository extends CrudRepository<WechatToken, Long> {
    WechatToken findByAppIdAndTokenType(String appId, int tokenType);
}
