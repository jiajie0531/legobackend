package com.delllogistics.repository.sys;

import com.delllogistics.entity.sys.WechatUserAccessToken;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * Created by jiajie on 05/05/2017.
 */
@Transactional
public interface WechatUserAccessTokenRepository extends CrudRepository<WechatUserAccessToken, Long> {

    WechatUserAccessToken findByAppId(String appId);

    WechatUserAccessToken findByAccessToken(String accessToken);

    WechatUserAccessToken findByOpenid(String openid);
}
