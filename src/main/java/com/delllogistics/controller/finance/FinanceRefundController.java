package com.delllogistics.controller.finance;

import com.delllogistics.dto.Result;
import com.delllogistics.dto.finance.FinanceRefundSearch;
import com.delllogistics.entity.Finance.FinanceRefund;
import com.delllogistics.entity.order.OrderMain;
import com.delllogistics.entity.order.OrderRefund;
import com.delllogistics.entity.user.User;
import com.delllogistics.service.finance.FinanceRefundService;
import com.delllogistics.spring.annotation.*;
import com.delllogistics.util.ResultUtil;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestJsonController
@RequestMapping("/financeRefund")
public class FinanceRefundController {

    private final FinanceRefundService financeRefundService;

    public FinanceRefundController(FinanceRefundService financeRefundService) {
        this.financeRefundService = financeRefundService;
    }

    /**
     * 查询退款单列表
     * @param financeRefundSearch 退款单搜索信息
     * @return 订单列表
     */
    @PostMapping("/findAll")
    @LogAnnotation(value="查询退款单列表")
    @JsonConvert(
            type = FinanceRefund.class,
            includes = {"id","code","orderRefund", "description","refundAmount","transactionCode","payTime"
                    ,"payChannel", "payType","payStatus","payRefundChannel","payStatus","user","createTime"},
            nest = {
                    @NestConvert(type = OrderRefund.class, includes = { "orderMain", "code"}),
                    @NestConvert(type = OrderMain.class, includes = {"id", "code"}),
                    @NestConvert(type = User.class, includes = {"id", "username"})
            })
    public Result findAll(@RequestBody FinanceRefundSearch financeRefundSearch) {
        Page<FinanceRefund> orders = financeRefundService.findAll(financeRefundSearch);
        return ResultUtil.success(orders);
    }



}
