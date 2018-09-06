package com.delllogistics.controller.sys;

import com.delllogistics.dto.Result;
import com.delllogistics.dto.sys.SysExpressSearch;
import com.delllogistics.entity.sys.SysExpress;
import com.delllogistics.entity.user.User;
import com.delllogistics.service.sys.SysExpressService;
import com.delllogistics.spring.annotation.CurrentUser;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.LogAnnotation;
import com.delllogistics.spring.annotation.RestJsonController;
import com.delllogistics.util.ResultUtil;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 *  快递公司
 * Created by calvin  2018/4/25
 */
@RestJsonController
@RequestMapping("/sysExpress")
public class SysExpressController {

    private final SysExpressService sysExpressService;

    public SysExpressController(SysExpressService sysExpressService) {
        this.sysExpressService = sysExpressService;
    }


    /**
     * 查询快递公司
     * @param sysExpressSearch 查询条件
     * @return 返回结果集
     */
    @PostMapping("/findAll")
    public Page<SysExpress> findAll(@RequestBody SysExpressSearch sysExpressSearch) {
        return sysExpressService.findAll(sysExpressSearch);
    }

    /**
     * 查询快递公司列表
     * @return 返回结果集
     */
    @PostMapping("/findAllSelect")
    @JsonConvert(
            type = SysExpress.class,
            includes = {"id", "name"}
    )
    public Iterable<SysExpress> findAllSelect() {
        return sysExpressService.findAllSelect();
    }

    /**
     * 保存快递公司
     * @param sysExpress 快递公司
     * @param bindingResult 绑定结果集
     * @param user 用户
     * @return 返回结果
     */
    @PostMapping("/save")
    @LogAnnotation(value="保存快递公司")
    public Result save(@Valid @RequestBody SysExpress sysExpress, BindingResult bindingResult, @CurrentUser User user) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(-1, bindingResult.getFieldError().getDefaultMessage());
        }

        sysExpressService.save(sysExpress,user);
        return ResultUtil.success();
    }

    /**
     * 删除快递公司
     * @param sysExpress 快递公司
     * @param user 用户
     * @return 返回结果
     */
    @PostMapping("/delete")
    @LogAnnotation(value="删除快递公司")
    public Result delete(@RequestBody SysExpress sysExpress, @CurrentUser User user) {
        sysExpressService.delete(sysExpress,user);
        return ResultUtil.success();
    }

    /**
     * 修改快递公司使用状态
     * @param sysExpress 实体
     * @param user 用户
     * @return 返回结果
     */
    @PostMapping("/saveUsed")
    @LogAnnotation(value="修改快递公司使用状态")
    public Result saveUsed(@RequestBody SysExpress sysExpress,  @CurrentUser User user) {
        sysExpressService.saveUsed(sysExpress,user);
        return ResultUtil.success();
    }

}
