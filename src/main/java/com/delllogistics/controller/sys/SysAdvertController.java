package com.delllogistics.controller.sys;

import com.delllogistics.dto.Result;
import com.delllogistics.dto.sys.SysAdvertSearch;
import com.delllogistics.entity.sys.SysAdvert;
import com.delllogistics.entity.user.User;
import com.delllogistics.service.sys.SysAdvertService;
import com.delllogistics.spring.annotation.CurrentUser;
import com.delllogistics.spring.annotation.LogAnnotation;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 广告管理
 * Created by guangxingju on 2017/11/2.
 */
@RestController
@RequestMapping("/sysAdvert")
public class SysAdvertController {
    private final SysAdvertService sysAdvertService;

    @Autowired
    public SysAdvertController(SysAdvertService sysAdvertService) {
        this.sysAdvertService = sysAdvertService;
    }

    /**
     * 查询广告
     * @param sysAdvertSearch 查询条件
     * @return 返回分页
     */
    @PostMapping("/findAll")
    @LogAnnotation(value="查询广告")
    public Page<SysAdvert> findAll(@RequestBody SysAdvertSearch sysAdvertSearch) {
        return sysAdvertService.findAll(sysAdvertSearch);
    }

    /**
     * 保存广告
     * @param sysAdvert 实体
     * @param user 用户
     * @return 返回结果集
     */
    @PostMapping("/save")
    @LogAnnotation(value="保存广告")
    public Result save(@Valid @RequestBody  SysAdvert sysAdvert,  BindingResult bindingResult,@CurrentUser User user) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(-1, bindingResult.getFieldError().getDefaultMessage());
        }
        sysAdvertService.save(sysAdvert,user);
        return ResultUtil.success();
    }

    /**
     * 修改广告使用状态
     * @param sysAdvert 广告
     * @param user 用户
     * @return 返回结果集
     */
    @PostMapping("/saveUsed")
    @LogAnnotation(value="修改广告使用状态")
    public Result saveUsed(@RequestBody SysAdvert sysAdvert,  @CurrentUser User user) {
        sysAdvertService.saveUsed(sysAdvert,user);
        return ResultUtil.success();
    }

    /**
     * 删除广告
     * @param sysAdvert 广告
     * @param user 用户
     * @return 返回结果集
     */
    @PostMapping("/delete")
    @LogAnnotation(value="删除广告")
    public Result delete(@RequestBody SysAdvert sysAdvert,  @CurrentUser User user) {
        sysAdvertService.delete(sysAdvert,user);
        return ResultUtil.success();
    }


}
