package com.delllogistics.controller.order;

import com.delllogistics.dto.Result;
import com.delllogistics.dto.order.OrderMainSearch;
import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.order.OrderActionLog;
import com.delllogistics.entity.order.OrderItem;
import com.delllogistics.entity.order.OrderMain;
import com.delllogistics.entity.sys.SysArea;
import com.delllogistics.entity.sys.SysFile;
import com.delllogistics.entity.user.User;
import com.delllogistics.service.order.OrderMainService;
import com.delllogistics.spring.annotation.*;
import com.delllogistics.util.ResultUtil;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestJsonController
@RequestMapping("/orderMain")
public class OrderMainController {

    private final OrderMainService orderMainService;

    public OrderMainController(OrderMainService orderMainService) {
        this.orderMainService = orderMainService;
    }


    /**
     * 根据订单状态查询订单列表
     * @param orderMainSearch 订单搜索信息
     * @param user 用户
     * @return 订单列表
     */
    @PostMapping("/findOrders")
    @JsonConvert(
            type = OrderMain.class,
            includes = {"id", "code", "orderItems","status","remarks","createTime","discountAmount"
                    ,"orderAmount", "rentalAmount","ensureAmount","deliveryFee" ,"dayNum"
                    ,"orderLogisticsType","deliveryDate","orderServiceEvaluateStatus"
                    ,"consignee","area","detailedAddress","phone","user"},
            nest = {
                    @NestConvert(type=SysArea.class,includes = {"id","name","level","sort","parent",}),
                    @NestConvert(type = OrderItem.class, includes = {"id", "goods", "quantity","price"}),
                    @NestConvert(type = User.class, includes = {"id", "username"}),
                    @NestConvert(type = Goods.class, includes = {"id", "goodsPic", "name", "ensurePrice", "rentalPrice"}),
                    @NestConvert(type = SysFile.class, includes = {"url"})
            })
    public Result findOrders(@RequestBody  OrderMainSearch orderMainSearch, @CurrentUser User user) {
        Page<OrderMain> orders = orderMainService.findOrders(orderMainSearch);
        return ResultUtil.success(orders);
    }


    /**
     * 根据订单ID查询订单
     * @param orderMainId 订单ID
     * @return 订单列表
     */
    @GetMapping("/findOne")
    @JsonConvert(
            type = OrderMain.class,
            includes = {"id", "code", "orderItems","status","remarks","createTime","discountAmount"
                    ,"orderAmount", "rentalAmount","ensureAmount","deliveryFee" ,"dayNum"
                    ,"orderLogisticsType","deliveryDate","orderServiceEvaluateStatus","orderActionLogs"
                    ,"consignee","area","detailedAddress","phone","user"},
            nest = {
                    @NestConvert(type= SysArea.class,includes = {"id","name","level","sort","parent",}),
                    @NestConvert(type= OrderActionLog.class,includes = {"id","orderActionType", "createTime","status"}),
                    @NestConvert(type = OrderItem.class, includes = {"id", "goods", "quantity","price"}),
                    @NestConvert(type = User.class, includes = {"id", "username"}),
                    @NestConvert(type = Goods.class, includes = {"id", "goodsPic", "name", "ensurePrice", "rentalPrice"}),
                    @NestConvert(type = SysFile.class, includes = {"url"})
            })
    public Result findOne(Long orderMainId) {
        OrderMain orderMain = orderMainService.findOne(orderMainId);
        return ResultUtil.success(orderMain);
    }




    /**
     * 取消订单
     * @param orderMain 订单信息
     * @param user 用户
     * @return
     */
    @PostMapping("/cancelOrder")
    @LogAnnotation(value="取消订单")
    public Result cancelOrder(@RequestBody OrderMain orderMain ,@CurrentUser User user){
        orderMainService.cancelOrder(orderMain,user);
        return ResultUtil.success();
    }



}
