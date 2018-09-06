package com.delllogistics.controller.app;

import com.delllogistics.dto.Result;
import com.delllogistics.entity.enums.OrderRefundStatus;
import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.logistics.LogisticsExpress;
import com.delllogistics.entity.order.*;
import com.delllogistics.entity.sys.SysArea;
import com.delllogistics.entity.sys.SysExpress;
import com.delllogistics.entity.sys.SysFile;
import com.delllogistics.entity.user.User;
import com.delllogistics.service.app.AppOrderRefundService;
import com.delllogistics.spring.annotation.*;
import com.delllogistics.util.ResultUtil;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 订单
 * Created by xzm on 2017-12-13.
 */
@RestJsonController
@RequestMapping("/appOrderRefund")
public class AppOrderRefundController {

    private final AppOrderRefundService appOrderRefundService;

    public AppOrderRefundController(AppOrderRefundService appOrderRefundService) {
        this.appOrderRefundService = appOrderRefundService;
    }

    /**
     * 退还申请
     * @param orderRefund   orderRefund
     * @param user          user
     * @return              Result
     */
    @PostMapping("/apply")
    @LogAnnotation(value="退还申请")
    public Result apply(@RequestBody OrderRefund orderRefund, @CurrentUser User user){
        appOrderRefundService.apply(orderRefund,user);
        return ResultUtil.success();
    }

    /**
     * 前端查询退还列表
     * @param page 页数
     * @param size 数量
     * @param orderRefundStatus 售后状态
     * @param user 用户
     * @return 订单列表
     */
    @GetMapping("/findAll")
    @JsonConvert(
            type = OrderItem.class,
            includes = {"id", "goods", "quantity","expiryTime","orderRefundStatus","orderMain"},
            nest = {
                    @NestConvert(type = OrderMain.class, includes = {"id", "code", "orderAmount","createTime","dayNum","status"}),
                    @NestConvert(type = Goods.class, includes = {"id", "goodsPic", "name", "ensurePrice", "rentalPrice"}),
                    @NestConvert(type = SysFile.class, includes = {"url"})
            })
    public Result findAll(int page, int size, OrderRefundStatus orderRefundStatus, @CurrentUser User user) {
        Page<OrderItem> orderItems = appOrderRefundService.findAll(page, size, orderRefundStatus, user);
        return ResultUtil.success(orderItems);
    }

    /**
     * 根据订单明细ID查询退还信息
     * @param orderItemId 订单明细ID
     * @return 退单信息
     */
    @GetMapping("/findOrderRefund")
    @JsonConvert(
            type = OrderRefund.class,
            includes = {"id","code", "orderItem", "orderMain", "orderRefundType","orderRefundReason","refundAmount","refundDescText"
                    ,"detailPics", "orderRefundStatus","user","createTime","orderRefundLogistics","chargeAmount","chargeType","chargeDescription","isCharge"},
            nest = {
                    @NestConvert(type=SysArea.class,includes = {"id","name","level","sort","parent",}),
                    @NestConvert(type=OrderActionLog.class,includes = {"id","user","remarks","status","orderActionType","createTime"}),
                    @NestConvert(type = OrderMain.class, includes = {"id", "code","status","remarks","createTime","discountAmount"
                            ,"orderAmount", "rentalAmount","ensureAmount","deliveryFee" ,"dayNum"
                            ,"orderLogisticsType","deliveryDate","orderServiceEvaluateStatus","orderActionLogs"
                            ,"consignee","area","detailedAddress","phone","user"}),
                    @NestConvert(type = OrderItem.class, includes = {"id", "goods","expiryTime", "quantity","price"}),
                    @NestConvert(type = OrderRefundLogistics.class, includes = {"id", "fetchTime", "fetchAddress","shippingAddress","logisticsExpress","logisticsCode","type"}),
                    @NestConvert(type = DeliveryAddress.class, includes = {"id","consignee","area","detailedAddress","phone"}),
                    @NestConvert(type = LogisticsExpress.class, includes = {"id","sysExpress"}),
                    @NestConvert(type = SysExpress.class, includes = {"id","name"}),
                    @NestConvert(type = SysArea.class,includes = {"id","name","level","sort","parent",}),
                    @NestConvert(type = User.class, includes = {"id", "username"}),
                    @NestConvert(type = Goods.class, includes = {"id", "goodsPic", "name", "ensurePrice", "rentalPrice"}),
                    @NestConvert(type = SysFile.class, includes = {"url"})
            })
    public Result findOrderRefund(Long orderItemId) {
        OrderRefund  orderRefund = appOrderRefundService.findOrderRefund(orderItemId);
        return ResultUtil.success(orderRefund);
    }


    /**
     * 买家同意退款
     * @param orderRefund 退款单信息
     * @param user 用户
     * @return 返回结果
     */
    @PostMapping("/userConfrimRefundAmount")
    @LogAnnotation(value="买家同意退款")
    public Result userConfrimRefundAmount(@RequestBody OrderRefund orderRefund ,@CurrentUser User user){
        appOrderRefundService.userConfrimRefundAmount(orderRefund,user);
        return ResultUtil.success();
    }
    /**
     * 获取退租金额-个人中心
     * @param user      user
     * @return          Result
     */
    @GetMapping("/findSumRefundAmount")
    public Result findSumRefundAmount(@CurrentUser User user){
        return ResultUtil.success(appOrderRefundService.findSumRefundAmount(user));
    }




}
