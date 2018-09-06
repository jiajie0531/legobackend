package com.delllogistics.controller.goods;

import com.delllogistics.dto.order.OrderItemEvalueteSearch;
import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.order.OrderItem;
import com.delllogistics.entity.order.OrderItemEvaluate;
import com.delllogistics.entity.sys.SysFile;
import com.delllogistics.entity.user.User;
import com.delllogistics.service.order.OrderItemEvaluateService;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.LogAnnotation;
import com.delllogistics.spring.annotation.NestConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestJsonController
@RequestMapping("/goodsComment")
public class GoodsCommentController {


    private final OrderItemEvaluateService orderItemEvaluateService;

    public GoodsCommentController(OrderItemEvaluateService orderItemEvaluateService) {
        this.orderItemEvaluateService = orderItemEvaluateService;
    }

    /**
     * 查询评论列表
     * @param orderItemEvalueteSearch 评论列表查询条件
     * @return 返回结果
     */
    @PostMapping("/findAll")
    @JsonConvert(
            type = OrderItemEvaluate.class,
            includes = {"id", "goodsScore", "content", "evaluateStatus", "detailPics", "createTime", "goods","orderItem","user"},
            nest = {
                    @NestConvert(type = Goods.class, includes = {"name","goodsPic"}),
                    @NestConvert(type = User.class, includes = {"username"}),
                    @NestConvert(type = SysFile.class, includes = {"url"}),
                    @NestConvert(type = OrderItem.class, includes = {"id"}),
            }
     )
    @LogAnnotation(value="查询评论列表")
    public Page<OrderItemEvaluate> findAll(@RequestBody OrderItemEvalueteSearch orderItemEvalueteSearch) {
        return orderItemEvaluateService.findAll(orderItemEvalueteSearch);
    }

}
