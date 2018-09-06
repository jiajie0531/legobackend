package com.delllogistics.controller.app;

import com.delllogistics.dto.Result;
import com.delllogistics.dto.app.OrderCount;
import com.delllogistics.entity.app.UserAddress;
import com.delllogistics.entity.enums.OrderMainStatus;
import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.order.OrderItem;
import com.delllogistics.entity.order.OrderMain;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.sys.SysArea;
import com.delllogistics.entity.sys.SysFile;
import com.delllogistics.entity.user.User;
import com.delllogistics.service.LogisticsDeliveryService;
import com.delllogistics.service.app.AppOrderService;
import com.delllogistics.spring.annotation.CurrentUser;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.NestConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import com.delllogistics.util.ResultUtil;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * 订单
 * Created by xzm on 2017-12-13.
 */
@RestJsonController
@RequestMapping("/appOrder")
public class AppOrderController {

    private final AppOrderService appOrderService;
    private final LogisticsDeliveryService logisticsDeliveryService;

    @Autowired
    public AppOrderController(AppOrderService appOrderService, LogisticsDeliveryService logisticsDeliveryService) {
        this.appOrderService = appOrderService;
        this.logisticsDeliveryService = logisticsDeliveryService;
    }

    /**
     * 提交订单
     *
     * @param orderMain 订单信息
     * @param user      用户
     * @return 订单id
     */
    @PostMapping("/submitOrder")
    public Result submitOrder(@RequestBody OrderMain orderMain, @CurrentUser User user) {
        Long rst = appOrderService.submitOrder(orderMain, user);
        if (rst < 1) {
            return ResultUtil.error(-1, "提交失败");
        }
        return ResultUtil.success(rst);
    }


    @PostMapping("/resultnotify")
    public String resultnotify(HttpServletRequest request) {
        return appOrderService.resultnotify(request);
    }

    /**
     * 查询用户订单数量
     *
     * @param user user
     * @return REsult
     */
    @GetMapping("/countOrderQuantity")
    public Result countOrderQuantity(@CurrentUser User user,@RequestParam Long companyId) {
        OrderCount orderCount = appOrderService.countOrderuantity(user,companyId);
        return ResultUtil.success(orderCount);
    }

    /**
     * 根据订单状态查询订单列表
     *
     * @param page            页数
     * @param size            数量
     * @param orderMainStatus 订单状态
     * @param user            用户
     * @return 订单列表
     */
    @GetMapping("/findOrders")
    @JsonConvert(
            type = OrderMain.class,
            includes = {"id", "code", "orderAmount", "orderItems", "status"},
            nest = {
                    @NestConvert(type = OrderItem.class, includes = {"id", "goods", "quantity", "orderItemEvaluateStatus"}),
                    @NestConvert(type = Goods.class, includes = {"id", "goodsPic", "name", "ensurePrice", "rentalPrice"}),
                    @NestConvert(type = SysFile.class, includes = {"url"})
            })
    public Result findOrders(int page, int size, OrderMainStatus orderMainStatus, @CurrentUser User user,@RequestParam Long companyId) {
        Page<OrderMain> orders = appOrderService.findOrders(page, size, orderMainStatus, user,companyId);
        return ResultUtil.success(orders);
    }

    /**
     * 根据订单ID查询订单信息
     *
     * @param orderId 订单ID
     * @return 订单信息
     */
    @GetMapping("/findOrder")
    @JsonConvert(
            type = OrderMain.class,
            includes = {"id", "code", "rentalAmount", "ensureAmount", "orderAmount", "consignee", "area", "detailedAddress", "phone",
                    "status", "orderItems", "address", "dayNum", "createTime","discountAmount","deliveryFee",
                    "orderLogisticsType", "deliveryDate", "deliveryTime", "orderServiceEvaluateStatus"},
            nest = {
                    @NestConvert(type = OrderItem.class, includes = {"id", "goods", "quantity", "orderItemEvaluateStatus"}),
                    @NestConvert(type = SysArea.class, includes = {"id", "name", "level", "sort", "parent",}),
                    @NestConvert(type = UserAddress.class, includes = {"id", "consignee", "phone", "detailedAddress", "area"}),
                    @NestConvert(type = Goods.class, includes = {"id", "goodsPic", "name", "ensurePrice", "rentalPrice"}),
                    @NestConvert(type = SysFile.class, includes = {"url"})
            })
    public Result findOrder(@RequestParam long orderId) {
        return ResultUtil.success(appOrderService.findOrder(orderId));
    }

    /**
     * 根据订单ID,商品ID 查询订单明细
     *
     * @param orderItemId 订单ID
     * @return 订单明细
     */
    @GetMapping("/findOrderItem")
    @JsonConvert(
            type = OrderItem.class,
            includes = {"id", "goods", "quantity","expiryTime","orderRefundStatus","orderMain"},
            nest = {
                    @NestConvert(type = OrderMain.class, includes = {"id", "code", "orderAmount","createTime","dayNum", "consignee", "area", "detailedAddress", "phone", "company"}),
                    @NestConvert(type = Goods.class, includes = {"id", "goodsPic", "name", "ensurePrice", "rentalPrice"}),
                     @NestConvert(type = SysFile.class, includes = {"url"}),
                     @NestConvert(type = Company.class, includes = {"id","name"}),
                    @NestConvert(type = SysArea.class, includes = {"id", "name", "level", "sort", "parent",}),
            })
    public Result findOrderItem(long orderItemId) {
        return ResultUtil.success(appOrderService.findOrderItem(orderItemId));
    }

    /**
     * 取消订单
     *
     * @param orderId 订单ID
     * @param user    用户
     * @return Result
     */
    @PostMapping("/cancelOrder")
    public Result cancelOrder(@RequestParam long orderId, @CurrentUser User user) {
        appOrderService.cancelOrder(orderId, user);
        return ResultUtil.success();
    }


    @GetMapping("/findOrderMainByItem")
    @JsonConvert(
            type = OrderMain.class,
            includes = {"id", "consignee", "area", "detailedAddress", "phone"},
            nest = {
                    @NestConvert(type = SysArea.class, includes = {"id", "name", "level", "sort", "parent",}),
            })
    public Result findOrderMainByItem(long orderItemId) {
        return ResultUtil.success(appOrderService.findOrderMainByItem(orderItemId));
    }



    /**
     * 支付订单
     *
     * @param orderId 订单ID
     * @param user    用户
     * @return Result
     */
    @PostMapping("/payOrder")
    public Result paylOrder(@RequestParam long orderId, @CurrentUser User user, HttpServletRequest request) {
        WxPayMpOrderResult wxPayMpOrderResultResult = appOrderService.payOrder(orderId, user, request);
        return ResultUtil.success(wxPayMpOrderResultResult);
    }

    /**
     * 确认收货
     *
     * @param orderId 订单ID
     * @param user    用户
     * @return Result
     */
    @PostMapping("/deliveryOrder")
    public Result deliveryOrder(@RequestParam long orderId, @CurrentUser User user) {
        logisticsDeliveryService.deliveryOrder(orderId, user);
        return ResultUtil.success();
    }

    /**
     * 续租
     *
     * @param orderId order id
     * @param user    user
     * @return Result
     */
    @PostMapping("/applyRenew")
    public Result applyRenew(@RequestParam long orderId, @RequestParam int buyNum, @CurrentUser User user) {
        appOrderService.applyRenew(orderId, buyNum, user);
        return ResultUtil.success();
    }


}
