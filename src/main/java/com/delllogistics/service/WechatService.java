package com.delllogistics.service;


import com.delllogistics.entity.enums.UserType;
import com.delllogistics.entity.sys.*;
import com.delllogistics.dto.ReWechatUserAccessToken;
import com.delllogistics.dto.WechatJsConfig;
import com.delllogistics.entity.enums.TokenType;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.user.WechatUser;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.ServiceException;
import com.delllogistics.exception.SystemException;
import com.delllogistics.repository.sys.WechatConfigRepository;
import com.delllogistics.repository.sys.WechatTokenRepository;
import com.delllogistics.repository.sys.WechatUserAccessTokenRepository;
import com.delllogistics.repository.sys.WechatUserRepository;
import com.delllogistics.repository.user.UserRepository;
import com.delllogistics.util.CRC32Util;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jiajie on 11/04/2017.
 */
@Service
public class WechatService {

    private static final String GET_INNER_OAUTH_URI = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
    private static final String GET_WEB_OAUTH_URI = "https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_login&state=STATE#wechat_redirect";
    private static final String GET_TOKEN_URI = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    private static final String GET_USER_URI = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
    private static final String GET_ACCESS_TOKEN_URI = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    private static final String GET_IP_URI = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=%s";
    private static final String GET_JS_API_TICKET_URI = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WechatTokenRepository wechatTokenRepository;

    @Autowired
    private WechatUserAccessTokenRepository wechatUserAccessTokenRepository;

    @Autowired
    private WechatUserRepository wechatUserRepository;

    @Autowired
    private WechatConfigRepository wechatConfigRepository;

    @Autowired
    private UserRepository userRepository;
    /**
     * 刷新access_token
     *
     * @param wechatConfig
     * @param code
     * @return
     */
    public ReWechatUserAccessToken doRefreshAccessToken(WechatConfig wechatConfig, String code) {
        ReWechatUserAccessToken reWechatUserAccessToken = getReWechatUserAccessToken(wechatConfig.getAppId(), wechatConfig.getAppSecret(), code);
        return reWechatUserAccessToken;
    }
    /**
     * 后台管理读取微信用户信息
     *
     * @param page  分页
     * @param size  分页大小
     * @param user  昵称
     */
    public Page<User> findWechatUsers(int page, int size, User user) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        Specification<User> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Predicate p1 = criteriaBuilder.equal(root.get("type").as(Integer.class), UserType.WECHATUSER.value);
            predicates.add(p1);
            if (!StringUtils.isEmpty(user.getUsername())) {
                Predicate p2 = criteriaBuilder.like(root.get("username"), "%" + user.getUsername() + "%");
                predicates.add(p2);
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return userRepository.findAll(specification, pageable);
    }
    /**
     * 读取用户的信息
     *
     * @param appId 微信应用的参数
     * @param code         用户授权后微信返回的code
     */
    public WechatUser getWechatUser(String appId, String code) {
        WechatConfig wechatConfig = wechatConfigRepository.findByAppId(appId);
        if (wechatConfig == null) {
            throw new ServiceException(ExceptionCode.NOT_FOUND, "未找到该appId对应的设置");
        }
        WxAccessToken wxAccessToken = getWxAccessToken(wechatConfig.getAppId(), wechatConfig.getAppSecret(), code);
        WxUser wxUser = getWxUser(wxAccessToken.getAccess_token(), wxAccessToken.getOpenid());
        WechatUser wechatUser = toWechatUser(wxUser);
        WechatUser byOpenId = wechatUserRepository.findByOpenId(wechatUser.getOpenId());
        if(byOpenId==null){
            wechatUserRepository.save(wechatUser);
            return wechatUser;
        }else{
            byOpenId.setNickName(wechatUser.getNickName());
            byOpenId.setProvince(wechatUser.getProvince());
            byOpenId.setCity(wechatUser.getCity());
            byOpenId.setCountry(wechatUser.getCountry());
            byOpenId.setSex(wechatUser.getSex());
            byOpenId.setHeadImageUrl(wechatUser.getHeadImageUrl());
            wechatUserRepository.save(byOpenId);
            return byOpenId;
        }

    }

    /**
     * 读取授权令牌(网页授权)
     *
     * @param appId     微信应用的Id
     * @param appSecret
     * @param code      用户授权后微信返回的code
     * @return
     */
    private ReWechatUserAccessToken getReWechatUserAccessToken(String appId, String appSecret, String code) {
        String uri = String.format(GET_TOKEN_URI, appId, appSecret, code);

        ReWechatUserAccessToken reWechatUserAccessToken;
        try {
            String result = Request.Get(uri).execute().returnContent().asString();
            checkApiError(result);
            reWechatUserAccessToken = objectMapper.readValue(result, ReWechatUserAccessToken.class);
        } catch (IOException e) {
            logger.error("请求微信［OAuthToken］发生错误", e);
            throw new SystemException(ExceptionCode.SYSTEM, "请求微信［OAuthToken］发生错误");
        }

        return reWechatUserAccessToken;
    }

    /**
     * 读取授权令牌(网页授权)
     *
     * @param appId 微信应用的Id
     * @param code  用户授权后微信返回的code
     */
    private WxAccessToken getWxAccessToken(String appId, String appSecret, String code) {
        String uri = String.format(GET_TOKEN_URI, appId, appSecret, code);

        WxAccessToken wxAccessToken;
        try {
            String result = Request.Get(uri).execute().returnContent().asString();
            checkApiError(result);
            wxAccessToken = objectMapper.readValue(result, WxAccessToken.class);
        } catch (IOException e) {
            logger.error("请求微信［OAuthToken］发生错误", e);
            throw new SystemException(ExceptionCode.SYSTEM, "请求微信［OAuthToken］发生错误");
        }

        return wxAccessToken;
    }

    /**
     * 读取微信AccessToken（接口调用）
     * 1.数据库读取token
     * 2.校验是否到期，到期则刷新
     * 3.校验是否有效，失效则刷新
     *
     * @param wechatConfig 微信应用的参数
     */
    public String getAccessToken(WechatConfig wechatConfig) {
        WechatToken token = wechatTokenRepository.findByAppIdAndTokenType(wechatConfig.getAppId(),
                TokenType.ACCESS_TOKEN.value);

        String accessToken;
        if (token != null) {
            accessToken = token.getToken();

            Date expiredAt = token.getExpiredAt();

            if (expiredAt.before(new Date())
                    || !checkAccessTokenAvailable(accessToken)) {
                accessToken = refreshAccessToken(wechatConfig, token);
            }
        } else {
            accessToken = refreshAccessToken(wechatConfig, null);
        }

        return accessToken;
    }

    private WechatUserAccessToken toWechatUserAccessToken(WechatConfig wechatConfig, ReWechatUserAccessToken reWechatUserAccessToken) {
        WechatUserAccessToken wechatUserAccessToken = new WechatUserAccessToken();

        wechatUserAccessToken.setAppId(wechatConfig.getAppId());

        Date now = new Date();
        now.setTime(now.getTime() + (long) reWechatUserAccessToken.getExpires_in() * 1000);
        wechatUserAccessToken.setExpiredAt(now);

        wechatUserAccessToken.setAccessToken(reWechatUserAccessToken.getAccess_token());
        wechatUserAccessToken.setExpiresIn(reWechatUserAccessToken.getExpires_in());
        wechatUserAccessToken.setOpenid(reWechatUserAccessToken.getOpenid());
        wechatUserAccessToken.setRefreshToken(reWechatUserAccessToken.getRefresh_token());
        wechatUserAccessToken.setScope(reWechatUserAccessToken.getScope());

        return wechatUserAccessToken;
    }

    @Transactional
    public ReWechatUserAccessToken getUserAccessToken(WechatConfig wechatConfig, String code) {
        ReWechatUserAccessToken reWechatUserAccessToken = getReWechatUserAccessToken(wechatConfig.getAppId(), wechatConfig.getAppSecret(), code);

        WechatUserAccessToken wechatUserAccessToken = wechatUserAccessTokenRepository.findByOpenid(reWechatUserAccessToken.getOpenid());

        if (wechatUserAccessToken != null) {
            Date now = new Date();
            now.setTime(now.getTime() + (long) reWechatUserAccessToken.getExpires_in() * 1000);
            wechatUserAccessToken.setExpiredAt(now);

            wechatUserAccessToken.setAccessToken(reWechatUserAccessToken.getAccess_token());
            wechatUserAccessToken.setRefreshToken(reWechatUserAccessToken.getRefresh_token());
            wechatUserAccessToken.setScope(reWechatUserAccessToken.getScope());

            wechatUserAccessTokenRepository.save(wechatUserAccessToken);
        } else {
            wechatUserAccessTokenRepository.save(toWechatUserAccessToken(wechatConfig, reWechatUserAccessToken));
        }

        saveWechatUser(reWechatUserAccessToken.getAccess_token(), reWechatUserAccessToken.getOpenid());

        return reWechatUserAccessToken;
    }

    @Transactional
    public void saveWechatUser(String access_token, String openid) {
        if (wechatUserRepository.findByOpenId(openid) == null) {
            WxUser wxUser = getWxUser(access_token, openid);
            WechatUser wechatUser = toWechatUser(wxUser);
            wechatUserRepository.save(wechatUser);
            User user = toUser(wechatUser);
            userRepository.save(user);
        }
    }

    public User toUser(WechatUser wechatUser) {
        User user = new User();
        user.setUsername(wechatUser.getNickName());
        user.setWechatUnionId(wechatUser.getOpenId());
        user.setCity(wechatUser.getCity());
        user.setGender(Integer.valueOf(wechatUser.getSex()));
        user.setDescription("wechat");
        user.setMobile("");
        return user;
    }
    /**
     * 读取微信js－sdk的config
     * 1. get js ticket
     * 2. 生成随机字符串，当前时间戳
     * 3. 签名
     *
     * @param wechatConfig 微信应用的参数
     * @param url          当前网页的URL，不包含#及其后面部分
     */
    public WechatJsConfig getJsConfig(WechatConfig wechatConfig, String url) {
        String nonceStr = CRC32Util.getHash();
        long timeStamp = System.currentTimeMillis();
        String signature = getSignature(wechatConfig, nonceStr, timeStamp, url);

        WechatJsConfig config = new WechatJsConfig();
        config.setAppId(wechatConfig.getAppId());
        config.setNonceStr(nonceStr);
        config.setTimestamp(timeStamp);
        config.setSignature(signature);
        return config;
    }

    /**
     * 读取微信js－sdk的config
     * 2. 生成随机字符串，当前时间戳
     * 3. 签名
     *
     * @param wechatConfig 微信应用的参数
     * @param tag          微信支付生成的订单标记字段
     */
    public WechatJsConfig getWechatPayJsConfig(WechatConfig wechatConfig, String tag) {
        String nonceStr = CRC32Util.getHash();
        long timeStamp = System.currentTimeMillis() / 1000;
        String signature = getWechatPaySignature(wechatConfig, nonceStr, timeStamp, tag);

        WechatJsConfig config = new WechatJsConfig();
        config.setAppId(wechatConfig.getAppId());
        config.setNonceStr(nonceStr);
        config.setTimestamp(timeStamp);
        config.setSignature(signature);
        return config;
    }


    private String getSignature(WechatConfig wechatConfig, String nonceStr, long timeStamp, String url) {
        String ticket = getJsApiTicket(wechatConfig);
        String origin = String.format("jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s",
                ticket, nonceStr, timeStamp, url);
        return DigestUtils.sha1Hex(origin);
    }

    /**
     * 读取微信JsApiTicket
     * 1.数据库读取ticket
     * 2.校验是否到期，到期则刷新
     *
     * @param wechatConfig 微信应用的参数
     */
    private String getJsApiTicket(WechatConfig wechatConfig) {
        WechatToken token = wechatTokenRepository.findByAppIdAndTokenType(wechatConfig.getAppId(),
                TokenType.JS_API_TICKET.value);

        String ticket;
        if (token != null) {
            ticket = token.getToken();

            Instant instant = token.getExpiredAt().toInstant();
            ZoneId zone = ZoneId.systemDefault();
            LocalDateTime expiredAt = LocalDateTime.ofInstant(instant, zone);

            // java.util.Date --> java.time.LocalDateTime
            if (expiredAt.isBefore(LocalDateTime.now())) {
                ticket = refreshJsApiTicket(wechatConfig, token);
            }
        } else {
            ticket = refreshJsApiTicket(wechatConfig, null);
        }

        return ticket;
    }

    private String refreshJsApiTicket(WechatConfig wechatConfig, WechatToken wechatToken) {
        String uri = String.format(GET_JS_API_TICKET_URI, getAccessToken(wechatConfig));

        JsApiTicket ticket;
        try {
            String result = Request.Get(uri).execute().returnContent().asString();
            checkApiError(result);
            ticket = objectMapper.readValue(result, JsApiTicket.class);
        } catch (IOException e) {
            logger.error("请求微信［JsApiTicket］发生错误", e);
            throw new SystemException(ExceptionCode.SYSTEM, "请求微信［JsApiTicket］发生错误");
        }

        Date now = new Date();
        if (wechatToken != null) {
            wechatToken.setToken(ticket.getTicket());
            now.setTime(now.getTime() + (long) ticket.getExpires_in() * 1000);
            wechatToken.setExpiredAt(now);
        } else {
            wechatToken = new WechatToken();
            wechatToken.setAppId(wechatConfig.getAppId());
            wechatToken.setTokenType(TokenType.JS_API_TICKET.value);
            wechatToken.setToken(ticket.getTicket());
            now.setTime(now.getTime() + (long) ticket.getExpires_in() * 1000);
            wechatToken.setExpiredAt(now);
        }
        wechatTokenRepository.save(wechatToken);

        return ticket.getTicket();
    }

    private String getWechatPaySignature(WechatConfig wechatConfig, String nonceStr, long timeStamp, String tag) {
        String origin = String.format("appId=%s&nonceStr=%s&package=%s&signType=MD5&timeStamp=%s&key=%s",
                wechatConfig.getAppId(), nonceStr, tag, timeStamp, wechatConfig.getPaySecret());
        return DigestUtils.md5Hex(origin);
    }

    private boolean checkAccessTokenAvailable(String accessToken) {
        String uri = String.format(GET_IP_URI, accessToken);

        boolean available = false;
        try {
            String result = Request.Get(uri).execute().returnContent().asString();
            available = result.contains("ip_list");
        } catch (IOException e) {
            logger.error("请求微信［IP］发生错误", e);
        }
        return available;
    }

    private String refreshAccessToken(WechatConfig wechatConfig, WechatToken wechatToken) {
        String uri = String.format(GET_ACCESS_TOKEN_URI, wechatConfig.getAppId(), wechatConfig.getAppSecret());

        AccessToken token;
        try {
            String result = Request.Get(uri).execute().returnContent().asString();
            checkApiError(result);
            token = objectMapper.readValue(result, AccessToken.class);
        } catch (IOException e) {
            logger.error("请求微信［AccessToken］发生错误", e);
            throw new SystemException(ExceptionCode.SYSTEM, "请求微信［AccessToken］发生错误");
        }

        Date now = new Date();
        if (wechatToken != null) {
            wechatToken.setToken(token.getAccess_token());
            now.setTime(now.getTime() + (long) token.getExpires_in() * 1000);
            wechatToken.setExpiredAt(now);
        } else {
            wechatToken = new WechatToken();
            wechatToken.setAppId(wechatConfig.getAppId());
            wechatToken.setTokenType(TokenType.ACCESS_TOKEN.value);
            wechatToken.setToken(token.getAccess_token());
            now.setTime(now.getTime() + (long) token.getExpires_in() * 1000);
            wechatToken.setExpiredAt(now);
        }
        wechatTokenRepository.save(wechatToken);

        return token.getAccess_token();
    }

    private WxUser getWxUser(String accessToken, String openId) {
        String uri = String.format(GET_USER_URI, accessToken, openId);

        WxUser wxUser;
        try {
            String result = Request.Get(uri).execute().returnContent().asString(Charset.forName("UTF-8"));
            checkApiError(result);
            wxUser = objectMapper.readValue(result, WxUser.class);
        } catch (IOException e) {
            logger.error("请求微信［UserInfo］发生错误", e);
            throw new SystemException(ExceptionCode.SYSTEM, "请求微信［UserInfo］发生错误");
        }

        return wxUser;
    }

    private void checkApiError(String result) {
        if (result.contains("errcode") && !result.contains("\"errcode\":0")) {
            logger.error("请求微信接口出错，错误原因： " + result);
            throw new ServiceException("请求微信接口发生错误");
        }
    }

    private WechatUser toWechatUser(WxUser wxUser) {
        if (wxUser == null) {
            return null;
        }
        WechatUser wechatUser = new WechatUser();
        wechatUser.setUnionId(wxUser.getUnionid());
        wechatUser.setOpenId(wxUser.getOpenid());
        wechatUser.setNickName(wxUser.getNickname());
        wechatUser.setSex(wxUser.getSex());
        wechatUser.setCountry(wxUser.getCountry());
        wechatUser.setProvince(wxUser.getProvince());
        wechatUser.setCity(wxUser.getCity());
        wechatUser.setHeadImageUrl(wxUser.getHeadimgurl());
        return wechatUser;
    }


    private static class JsApiTicket {
        /**
         * jsApiTicket
         */
        private String ticket;

        /**
         * 凭证失效时间（秒）
         */
        private int expires_in;

        public String getTicket() {
            return ticket;
        }

        public void setTicket(String ticket) {
            this.ticket = ticket;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }
    }

    private static class AccessToken {
        /**
         * 接口调用凭证
         */
        private String access_token;

        /**
         * 凭证失效时间（秒）
         */
        private int expires_in;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }
    }

    private static class WxAccessToken {
        /**
         * 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionId是唯一的。
         */
        private String unionid;
        /**
         * 用户的标识，对当前开发者帐号唯一
         */
        private String openid;
        /**
         * 授权访问令牌
         */
        private String access_token;

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }
    }

    private static class WxUser {
        /**
         * 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionId是唯一的。
         */
        private String unionid;
        /**
         * 用户的标识，对当前开发者帐号唯一
         */
        private String openid;
        /**
         * 昵称
         */
        private String nickname;
        /**
         * 性别
         */
        private String sex;
        /**
         * 国家
         */
        private String country;
        /**
         * 省份
         */
        private String province;
        /**
         * 城市
         */
        private String city;
        /**
         * 头像地址(在用户修改微信头像后，旧的微信头像URL将会失效，
         * 因此开发者应该自己在获取用户信息后，将头像图片保存下来，
         * 避免微信头像URL失效后的异常情况。)
         */
        private String headimgurl;

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
        }
    }
}
