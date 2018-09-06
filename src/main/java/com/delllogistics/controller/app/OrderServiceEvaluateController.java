package com.delllogistics.controller.app;

import com.delllogistics.dto.Result;
import com.delllogistics.entity.order.OrderServiceEvaluate;
import com.delllogistics.entity.user.User;
import com.delllogistics.service.app.OrderServiceEvaluteService;
import com.delllogistics.spring.annotation.CurrentUser;
import com.delllogistics.spring.annotation.RestJsonController;
import com.delllogistics.util.ResultUtil;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 *  订单详细评价
 * Created by calvin  2018/1/23
 */
@RestJsonController
@RequestMapping("/app/orderServiceEvaluate")
public class OrderServiceEvaluateController {

    private final OrderServiceEvaluteService orderServiceEvaluteService;

    public OrderServiceEvaluateController(OrderServiceEvaluteService orderServiceEvaluteService) {
        this.orderServiceEvaluteService = orderServiceEvaluteService;
    }

    /**
     * 提交订单详细评价
     * @param orderServiceEvaluate
     * @param user
     * @param bindingResult
     * @return
     */
    @PostMapping("/submit")
    public Result submit(@RequestBody OrderServiceEvaluate orderServiceEvaluate, @CurrentUser User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(-1, bindingResult.getFieldError().getDefaultMessage());
        }
        int rst =   orderServiceEvaluteService.submit(orderServiceEvaluate,user);
        if(rst<1){
            return ResultUtil.error(-1,"提交失败");
        }
        return ResultUtil.success();
    }







}
