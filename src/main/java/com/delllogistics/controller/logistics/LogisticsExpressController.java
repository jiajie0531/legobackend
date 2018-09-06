package com.delllogistics.controller.logistics;

import com.delllogistics.dto.BaseSelect;
import com.delllogistics.dto.Result;
import com.delllogistics.dto.logistics.LogisticsExpressSearch;
import com.delllogistics.entity.logistics.LogisticsExpress;
import com.delllogistics.entity.sys.SysExpress;
import com.delllogistics.entity.user.User;
import com.delllogistics.service.logistics.LogisticsExpressService;
import com.delllogistics.spring.annotation.*;
import com.delllogistics.util.ResultUtil;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

/**
 *  快递管理
 * Created by calvin  2018/4/25
 */
@RestJsonController
@RequestMapping("/logisticsExpress")
public class LogisticsExpressController {

    private final LogisticsExpressService logisticsExpressService;

    public LogisticsExpressController(LogisticsExpressService logisticsExpressService) {
        this.logisticsExpressService = logisticsExpressService;
    }

    /**
     * 查询快递管理
     * @param logisticsExpressSearch 查询条件
     * @return 返回结果集
     */
    @PostMapping("/findAll")
    @JsonConvert(
            type = LogisticsExpress.class,
            includes = {"id", "sysExpress","isUsed","createTime","updateTime"},
            nest = {
                    @NestConvert(type = SysExpress.class, includes = {"id","name"})
            }
    )
    public Page<LogisticsExpress> findAll(@RequestBody LogisticsExpressSearch logisticsExpressSearch) {
        return logisticsExpressService.findAll(logisticsExpressSearch);
    }

    /**
     * 查询快递列表
     * @param companyId 企业id
     * @return 返回结果集
     */
    @GetMapping("/findAllSelect")
    @JsonConvert(
            type = BaseSelect.class,
            includes = {"id", "name"}
    )
    public List findAllSelect(Long companyId) {
        return logisticsExpressService.findAllSelect(companyId);
    }

    /**
     * 保存快递管理
     * @param logisticsExpress 快递管理
     * @param bindingResult 绑定结果集
     * @param user 用户
     * @return 返回结果
     */
    @PostMapping("/save")
    @LogAnnotation(value="保存快递管理")
    public Result save(@Valid @RequestBody LogisticsExpress logisticsExpress, BindingResult bindingResult, @CurrentUser User user) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(-1, bindingResult.getFieldError().getDefaultMessage());
        }
        logisticsExpressService.save(logisticsExpress,user);
        return ResultUtil.success();
    }

    /**
     * 删除快递管理
     * @param logisticsExpress 快递管理
     * @param user 用户
     * @return 返回结果
     */
    @PostMapping("/delete")
    @LogAnnotation(value="删除快递管理")
    public Result delete(@RequestBody LogisticsExpress logisticsExpress, @CurrentUser User user) {
        logisticsExpressService.delete(logisticsExpress,user);
        return ResultUtil.success();
    }

    /**
     * 修改快递管理使用状态
     * @param logisticsExpress 快递管理
     * @param user 用户
     * @return 返回结果
     */
    @PostMapping("/saveUsed")
    @LogAnnotation(value="修改快递管理使用状态")
    public Result saveUsed(@RequestBody LogisticsExpress logisticsExpress,  @CurrentUser User user) {
        logisticsExpressService.saveUsed(logisticsExpress,user);
        return ResultUtil.success();
    }

}
