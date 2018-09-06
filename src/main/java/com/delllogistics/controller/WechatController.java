package com.delllogistics.controller;


import com.delllogistics.dto.ReWechatToken;
import com.delllogistics.dto.ReWechatUserAccessToken;
import com.delllogistics.dto.Result;
import com.delllogistics.dto.WechatJsConfig;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.sys.WechatConfig;
import com.delllogistics.entity.user.WechatUser;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.ServiceException;
import com.delllogistics.service.WechatConfigService;
import com.delllogistics.service.WechatService;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.NestConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**微信
 * Created by jiajie on 11/04/2017.
 */
@RestJsonController
@RequestMapping("/wechat")
public class WechatController {

    private final WechatService wechatService;

    private final WechatConfigService wechatConfigService;

    @Autowired
    public WechatController(WechatService wechatService, WechatConfigService wechatConfigService) {
        this.wechatService = wechatService;
        this.wechatConfigService = wechatConfigService;
    }


    /**
     * 读取微信AccessToken（接口调用）
     *
     * @param appId 微信应用的Id
     */
    @RequestMapping(value = "/getAccessToken", method = RequestMethod.POST)
    public ReWechatToken getAccessToken(@RequestParam String appId) {
        ReWechatToken reWechatToken = new ReWechatToken();

        WechatConfig wechatConfig = wechatConfigService.getWechatConfigById(appId);
        if (wechatConfig == null) {
            throw new ServiceException(ExceptionCode.NOT_FOUND, "未找到该appId对应的设置");
        }
        reWechatToken.setToken(wechatService.getAccessToken(wechatConfig));
        return reWechatToken;
    }

    /**
     * get user access_token
     *
     * @param appId 微信应用的Id
     * @param code  用户授权后微信返回的code
     */
    @RequestMapping(value = "/getUserAccessToken", method = RequestMethod.POST)
    public ReWechatUserAccessToken getUserAccessToken(@RequestParam String appId, @RequestParam String code) {
        WechatConfig wechatConfig = wechatConfigService.getWechatConfigById(appId);
        if (wechatConfig == null) {
            throw new ServiceException(ExceptionCode.NOT_FOUND, "未找到该appId对应的设置");
        }

        return wechatService.getUserAccessToken(wechatConfig, code);
    }

    /**
     * 读取用户的信息
     *
     * @param appId 微信应用的Id
     * @param code  用户授权后微信返回的code
     */
    @RequestMapping(value = "/getWechatUser", method = RequestMethod.POST,produces = {"application/json;charset=utf-8"})
    public WechatUser getWechatUser(@RequestParam String appId, @RequestParam String code) {
        return wechatService.getWechatUser(appId, code);
    }

    /**
     * 读取微信js－sdk的config
     *
     * @param appId 微信应用的Id
     * @param url   当前网页的URL，不包含#及其后面部分
     */
    @RequestMapping(value = "/getJsConfig", method = RequestMethod.POST)
    public WechatJsConfig getJsConfig(@RequestParam String appId, @RequestParam String url) {
        WechatConfig wechatConfig = wechatConfigService.getWechatConfigById(appId);
        if (wechatConfig == null) {
            throw new ServiceException(ExceptionCode.NOT_FOUND, "未找到该appId对应的设置");
        }

        return wechatService.getJsConfig(wechatConfig, url);
    }
    @PostMapping("/findwechatUsers")
    public Page<User> findWechatUsers(int page, int size, User pagination){
        return wechatService.findWechatUsers(page, size, pagination);
    }

    @GetMapping("/getWechatConfig")
    @JsonConvert(type = WechatConfig.class,includes = {"company"},
            nest = {@NestConvert(type=Company.class,includes = {"id"})})
    public Result getWechatConfig(String appId){
        WechatConfig wechatConfig = wechatConfigService.getWechatConfigById(appId);
        return ResultUtil.success(wechatConfig);
    }

}
