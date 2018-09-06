package com.delllogistics.repository.sys;

import com.delllogistics.entity.user.WechatUser;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * Created by jiajie on 06/05/2017.
 */
@Transactional
public interface WechatUserRepository extends CrudRepository<WechatUser, Long> {

    WechatUser findByOpenId(String openId);
}
