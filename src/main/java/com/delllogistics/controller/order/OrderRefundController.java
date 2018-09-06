package com.delllogistics.controller.order;

import com.delllogistics.dto.Result;
import com.delllogistics.dto.order.OrderRefundSearch;
import com.delllogistics.entity.enums.OrderRefundLogisticsType;
import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.logistics.LogisticsExpress;
import com.delllogistics.entity.order.*;
import com.delllogistics.entity.sys.SysArea;
import com.delllogistics.entity.sys.SysExpress;
import com.delllogistics.entity.sys.SysFile;
import com.delllogistics.entity.user.User;
import com.delllogistics.service.order.OrderRefundService;
import com.delllogistics.spring.annotation.*;
import com.delllogistics.util.ResultUtil;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;

@RestJsonController
@RequestMapping("/orderRefund")
public class OrderRefundController {

    private final OrderRefundService orderRefundService;

    public OrderRefundController(OrderRefundService orderRefundService) {
        this.orderRefundService = orderRefundService;
    }

    /**
     * 查询退单列表
     * @param orderRefundSearch 退单搜索信息
     * @return 订单列表
     */
    @PostMapping("/findAll")
    @JsonConvert(
            type = OrderRefund.class,
            includes = {"id","code", "orderItem", "orderMain", "orderRefundType","orderRefundReason","refundAmount", "orderRefundStatus","user","createTime","orderRefundLogistics"},
            nest = {
                    @NestConvert(type=SysArea.class,includes = {"id","name","level","sort","parent",}),
                    @NestConvert(type = OrderMain.class, includes = {"id", "code","user"}),
                    @NestConvert(type = OrderItem.class, includes = {"id", "goods", "quantity","price"}),
                    @NestConvert(type = OrderRefundLogistics.class, includes = {"id","type"}),
                    @NestConvert(type = User.class, includes = {"id", "username"}),
                    @NestConvert(type = Goods.class, includes = {"id", "goodsPic", "name", "ensurePrice", "rentalPrice"}),
                    @NestConvert(type = SysFile.class, includes = {"url"})
            })
    public Result findAll(@RequestBody OrderRefundSearch orderRefundSearch) {
        Page<OrderRefund> orders = orderRefundService.findAll(orderRefundSearch);
        return ResultUtil.success(orders);
    }

    /**
     * 查询退单列表
     * @param orderRefundSearch 退单搜索信息
     * @return 订单列表
     */
    @PostMapping("/findOrderRefund")
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
                    @NestConvert(type = OrderItem.class, includes = {"id", "goods", "quantity","price"}),
                    @NestConvert(type = OrderRefundLogistics.class, includes = {"id", "fetchTime", "fetchAddress","shippingAddress","logisticsExpress","logisticsCode","type"}),
                    @NestConvert(type = DeliveryAddress.class, includes = {"id","consignee","area","detailedAddress","phone"}),
                    @NestConvert(type = LogisticsExpress.class, includes = {"id","sysExpress"}),
                    @NestConvert(type = SysExpress.class, includes = {"id","name"}),
                    @NestConvert(type = SysArea.class,includes = {"id","name","level","sort","parent",}),
                    @NestConvert(type = User.class, includes = {"id", "username"}),
                    @NestConvert(type = Goods.class, includes = {"id", "goodsPic", "name", "ensurePrice", "rentalPrice"}),
                    @NestConvert(type = SysFile.class, includes = {"url"})
            })
    public Result findOrderRefund(@RequestBody OrderRefundSearch orderRefundSearch) {
        OrderRefund orders = orderRefundService.findOrderRefund(orderRefundSearch);
        return ResultUtil.success(orders);
    }

    /**
     * 卖家确认收货
     * @param orderRefund 退款单信息
     * @param user 用户
     * @return 返回结果
     */
    @PostMapping("/agreeOrderRefund")
    @LogAnnotation(value="卖家确认收货")
    public Result agreeOrderRefund(@RequestBody OrderRefund orderRefund ,@CurrentUser User user){
        orderRefundService.agreeOrderRefund(orderRefund,user);
        return ResultUtil.success();
    }


    /**
     * 确认退款
     * @param orderRefund 退款单信息
     * @param user 用户
     * @return 返回结果
     */
    @PostMapping("/sellerConfirm")
    @LogAnnotation(value="确认退款")
    public Result sellerConfirm(@RequestBody OrderRefund orderRefund ,@CurrentUser User user){
        orderRefundService.sellerConfirm(orderRefund,user);
        return ResultUtil.success();
    }


    @PostMapping("/resultnotify")
    public String resultnotify(HttpServletRequest request){
        String xmlResult ;
        try {
            xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        } catch (IOException e) {
            return WxPayNotifyResponse.fail("XML读取异常");
        }
        return orderRefundService.resultnotify(xmlResult);
    }
}
