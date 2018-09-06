package com.delllogistics.controller.logistics;

import com.delllogistics.dto.Result;
import com.delllogistics.dto.logistics.LogisticsDeliveryLogSearch;
import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.logistics.LogisticsDeliveryLog;
import com.delllogistics.entity.logistics.LogisticsExpress;
import com.delllogistics.entity.order.OrderActionLog;
import com.delllogistics.entity.order.OrderItem;
import com.delllogistics.entity.order.OrderMain;
import com.delllogistics.entity.sys.SysArea;
import com.delllogistics.entity.sys.SysExpress;
import com.delllogistics.entity.sys.SysFile;
import com.delllogistics.entity.user.User;
import com.delllogistics.service.logistics.LogisticsDeliveryLogService;
import com.delllogistics.spring.annotation.*;
import com.delllogistics.util.ResultUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestJsonController
@RequestMapping("/logisticsDeliveryLog")
public class LogisticsDeliveryLogController {

    private final LogisticsDeliveryLogService logisticsDeliveryLogService;

    public LogisticsDeliveryLogController(LogisticsDeliveryLogService logisticsDeliveryLogService) {
        this.logisticsDeliveryLogService = logisticsDeliveryLogService;
    }


    /**
     * 订单物流信息保存
     * @param logisticsDeliveryLog 记录
     * @param user 用户
     * @return 返回结果
     */
    @PostMapping("/businessShipped")
    @LogAnnotation(value="一键发货")
    public Result businessShipped(@RequestBody LogisticsDeliveryLog logisticsDeliveryLog , @CurrentUser User user){
        logisticsDeliveryLogService.businessShipped(logisticsDeliveryLog,user);
        return ResultUtil.success();
    }



    /**
     * 订单物流信息保存
     * @param logisticsDeliveryLogSearch  搜索条件
     * @return 返回结果
     */
    @PostMapping("/findAll")
    @JsonConvert(
            type = LogisticsDeliveryLog.class,
            includes = {"id", "logisticsExpress", "logisticsCode", "deliveryStatus","deliveryType","remarks","createTime","createUser" },
            nest = {
                    @NestConvert(type= LogisticsExpress.class,includes = {"id","sysExpress",}),
                    @NestConvert(type= SysExpress.class,includes = {"id","name"}),
                    @NestConvert(type = User.class, includes = {"id", "username"}),
     })
    public Result findAll(@RequestBody LogisticsDeliveryLogSearch logisticsDeliveryLogSearch){
        return ResultUtil.success(logisticsDeliveryLogService.findAll(logisticsDeliveryLogSearch));
    }









}
