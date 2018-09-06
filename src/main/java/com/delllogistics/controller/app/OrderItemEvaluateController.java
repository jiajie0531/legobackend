package com.delllogistics.controller.app;

import com.delllogistics.dto.Result;
import com.delllogistics.entity.user.MembershipRank;
import com.delllogistics.entity.order.OrderItemEvaluate;
import com.delllogistics.entity.sys.SysFile;
import com.delllogistics.entity.user.User;
import com.delllogistics.service.app.AppOrderItemEvaluateService;
import com.delllogistics.spring.annotation.CurrentUser;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.NestConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 订单详细评价
 * Created by calvin  2018/1/23
 */
@RestJsonController
@RequestMapping("/app/orderItemEvaluate")
public class OrderItemEvaluateController {

    private final AppOrderItemEvaluateService appOrderItemEvaluateService;

    @Autowired
    public OrderItemEvaluateController(AppOrderItemEvaluateService appOrderItemEvaluateService) {
        this.appOrderItemEvaluateService = appOrderItemEvaluateService;
    }

    /**
     * 获取商品评价内容
     * @param goodsId   goodsId
     * @param page      page
     * @param size      size
     * @return  Page
     */
    @JsonConvert(
            type = OrderItemEvaluate.class,
            includes = {"id", "content", "evaluateStatus", "goodsScore", "createTime","detailPics","user"},
            nest = {
                    @NestConvert(type = User.class, includes = {"id", "username", "membershipRank","photo"}),
                    @NestConvert(type = MembershipRank.class, includes = { "icon", "name"}),
                    @NestConvert(type = SysFile.class, includes = {"url"})
            })
    @GetMapping("/findOrderItemEvaluatesByGoods")
    public Page<OrderItemEvaluate> findOrderItemEvaluatesByGoods(Long goodsId, int page, int size) {
        return appOrderItemEvaluateService.findOrderItemEvaluatesByGoods(goodsId, page, size);
    }

    @PostMapping("/submit")
    public Result submit(@RequestBody OrderItemEvaluate orderItemEvaluate, @CurrentUser User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(-1, bindingResult.getFieldError().getDefaultMessage());
        }
        int rst = appOrderItemEvaluateService.submit(orderItemEvaluate, user);
        if (rst < 1) {
            return ResultUtil.error(-1, "提交失败");
        }
        return ResultUtil.success();
    }

    @PostMapping("/submitShare")
    public Result submitShare(@RequestBody OrderItemEvaluate orderItemEvaluate, @CurrentUser User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(-1, bindingResult.getFieldError().getDefaultMessage());
        }
        int rst = appOrderItemEvaluateService.submitShare(orderItemEvaluate, user);
        if (rst < 1) {
            return ResultUtil.error(-1, "提交失败");
        }
        return ResultUtil.success();
    }


}
