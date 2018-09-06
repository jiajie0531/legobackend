package com.delllogistics.controller.order;

import com.delllogistics.dto.Result;
import com.delllogistics.entity.enums.SellGoodsDateType;
import com.delllogistics.service.order.OrderItemService;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Order Item.<br/>
 * User: jiajie<br/>
 * Date: 04/03/2018<br/>
 * Time: 12:14 PM<br/>
 */
@RestController
@RequestMapping("/orderItem")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping("/findSellCount")
    public Result findSellCount(@RequestParam(value = "SellGoodsDateType")SellGoodsDateType sellGoodsDateType){
        return ResultUtil.success(orderItemService.findSellCount(sellGoodsDateType));
    }
}
