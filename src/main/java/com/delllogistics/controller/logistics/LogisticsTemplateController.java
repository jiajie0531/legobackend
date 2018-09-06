package com.delllogistics.controller.logistics;

import com.delllogistics.dto.Result;
import com.delllogistics.dto.logistics.LogisticsTemplateSearch;
import com.delllogistics.entity.enums.LogisticsDeliveryTime;
import com.delllogistics.entity.enums.LogisticsDeliveryType;
import com.delllogistics.entity.enums.LogisticsFreightType;
import com.delllogistics.entity.enums.LogisticsPriceType;
import com.delllogistics.entity.logistics.LogisticsTemplate;
import com.delllogistics.entity.logistics.LogisticsTemplateItem;
import com.delllogistics.entity.sys.SysArea;
import com.delllogistics.entity.user.User;
import com.delllogistics.service.logistics.LogisticsTemplateService;
import com.delllogistics.spring.annotation.*;
import com.delllogistics.util.EnumHelper;
import com.delllogistics.util.EnumItem;
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
 *  物流模版
 * Created by calvin  2018/4/25
 */
@RestJsonController
@RequestMapping("/logisticsTemplate")
public class LogisticsTemplateController {

    private final LogisticsTemplateService logisticsTemplateService;

    public LogisticsTemplateController(LogisticsTemplateService logisticsTemplateService) {
        this.logisticsTemplateService = logisticsTemplateService;
    }

    /**
     * 查询物流模版
     * @param logisticsTemplateSearch 查询条件
     * @return 返回结果集
     */
    @PostMapping("/findAll")
    @JsonConvert(
            type = LogisticsTemplate.class,
            includes = {"id", "name","deliveryTime","shippingType","freightType","deliveryType","priceType","area","createTime","updateTime","isUsed","logisticsTemplateItems","defualtItem"},
            nest = {
                    @NestConvert(type=SysArea.class,includes = {"id","name","level","sort","parent",}),
                    @NestConvert(type=LogisticsTemplateItem.class,includes = {"id","deliveryTo","firstPrice","nextPrice","firstValue","nextValue","defualt","deliveryType"}),
            }
    )
    public Page<LogisticsTemplate> findAll(@RequestBody LogisticsTemplateSearch logisticsTemplateSearch) {
        return logisticsTemplateService.findAll(logisticsTemplateSearch);
    }
    @GetMapping("/findOne")
    @JsonConvert(
            type = LogisticsTemplate.class,
            includes = {"id", "name","deliveryTime","shippingType","freightType","deliveryType","priceType","area","createTime","updateTime","isUsed","logisticsTemplateItems","defualtItem"},
            nest = {
                    @NestConvert(type=SysArea.class,includes = {"id","name","level","sort","parent",}),
                    @NestConvert(type=LogisticsTemplateItem.class,includes = {"id","deliveryTo","firstPrice","nextPrice","firstValue","nextValue","defualt","deliveryType"}),
            }
    )
    public  Result findOne(Long templateId) {
        LogisticsTemplate logisticsTemplate =  logisticsTemplateService.findOne(templateId);
        if(logisticsTemplate!=null){
            return ResultUtil.success(logisticsTemplate);
        }
        return ResultUtil.error(-1, "没有数据");
    }

    /**
     * 获取发货时间
     * @return 返回结果集
     */
    @PostMapping("/getDeliveryTime")
    public List<EnumItem> getDeliveryTime() {
        return EnumHelper.getEnumList(LogisticsDeliveryTime.class);
    }
    /**
     * 获取计费类型
     * @return 返回结果集
     */
    @PostMapping("/getPriceType")
    public List<EnumItem> getPriceType() {
        return EnumHelper.getEnumList(LogisticsPriceType.class);
    }
    /**
     * 获取运费类型
     * @return 返回结果集
     */
    @PostMapping("/getFreightType")
    public List<EnumItem> getFreightType() {
        return EnumHelper.getEnumList(LogisticsFreightType.class);
    }
    /**
     * 获取运送方式
     * @return 返回结果集
     */
    @PostMapping("/getShippingType")
    public List<EnumItem> getShippingType() {
        return EnumHelper.getEnumList(LogisticsDeliveryType.class);
    }

    /**
     * 查询物流模版列表
     * @return 返回结果集
     */
    @PostMapping("/findAllSelect")
    @JsonConvert(
            type = LogisticsTemplate.class,
            includes = {"id", "name"}
    )
    public Iterable<LogisticsTemplate> findAllSelect(Long companyId) {
        return logisticsTemplateService.findAllSelect(companyId);
    }

    /**
     * 保存物流模版
     * @param logisticsTemplate 物流模版
     * @param user 用户
     * @return 返回结果
     */
    @PostMapping("/save")
    @LogAnnotation(value="保存物流模版")
    public Result save(@Valid @RequestBody LogisticsTemplate logisticsTemplate, BindingResult bindingResult, @CurrentUser User user) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(-1, bindingResult.getFieldError().getDefaultMessage());
        }
        logisticsTemplateService.save(logisticsTemplate,user);
        return ResultUtil.success();
    }

    /**
     * 删除物流模版
     * @param logisticsTemplate 物流模版
     * @param user 用户
     * @return 返回结果
     */
    @PostMapping("/delete")
    @LogAnnotation(value="删除物流模版")
    public Result delete(@RequestBody LogisticsTemplate logisticsTemplate, @CurrentUser User user) {
        logisticsTemplateService.delete(logisticsTemplate,user);
        return ResultUtil.success();
    }

    /**
     * 修改物流模版使用状态
     * @param logisticsTemplate 物流模版
     * @param user 用户
     * @return 返回结果
     */
    @PostMapping("/saveUsed")
    @LogAnnotation(value="修改物流模版使用状态")
    public Result saveUsed(@RequestBody LogisticsTemplate logisticsTemplate,  @CurrentUser User user) {
        logisticsTemplateService.saveUsed(logisticsTemplate,user);
        return ResultUtil.success();
    }

}
