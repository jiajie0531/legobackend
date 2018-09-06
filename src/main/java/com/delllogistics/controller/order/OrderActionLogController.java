package com.delllogistics.controller.order;

import com.delllogistics.dto.Result;
import com.delllogistics.entity.order.OrderActionLog;
import com.delllogistics.entity.order.OrderMain;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.enums.OrderActionType;
import com.delllogistics.service.order.OrderActionLogService;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.NestConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import com.delllogistics.util.ResultUtil;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestJsonController
@RequestMapping("/orderActionLog")
public class OrderActionLogController {

    private final OrderActionLogService orderActionLogService;

    public OrderActionLogController(OrderActionLogService orderActionLogService) {
        this.orderActionLogService = orderActionLogService;
    }

    /**
     * 获取订单操作信息
     * @param page
     * @param size
     * @param orderMainId 订单ID
     * @param orderActionType 订单类型
     * @return
     */
    @GetMapping("/findAll")
    @JsonConvert(
            type = OrderActionLog.class,
            includes = {"id", "orderMain","orderActionType", "user","createTime","remarks","status"},
            nest = {
                    @NestConvert(type=User.class,includes = {"id","username"}),
                    @NestConvert(type = OrderMain.class, includes = {"id", "code"}),
            })
    public Result findAll( int page,int size  , Long orderMainId, OrderActionType orderActionType) {
        Page<OrderActionLog> orderActionLogs = orderActionLogService.findAll( page,size,orderMainId,orderActionType);
        return ResultUtil.success(orderActionLogs);
    }




}
