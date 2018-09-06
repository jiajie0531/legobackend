package com.delllogistics.controller.finance;

import com.delllogistics.dto.Result;
import com.delllogistics.dto.finance.FinanceReceivableSearch;
import com.delllogistics.entity.Finance.FinanceReceivable;
import com.delllogistics.entity.order.OrderMain;
import com.delllogistics.entity.user.User;
import com.delllogistics.service.finance.FinanceReceivableService;
import com.delllogistics.spring.annotation.*;
import com.delllogistics.util.ResultUtil;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestJsonController
@RequestMapping("/financeReceivable")
public class FinanceReceivableController {

    private final FinanceReceivableService financeReceivableService;

    public FinanceReceivableController(FinanceReceivableService financeReceivableService) {
        this.financeReceivableService = financeReceivableService;
    }

    /**
     * 查询收款单列表
     * @param financeReceivableSearch 收款单搜索信息
     * @return 订单列表
     */
    @PostMapping("/findAll")
    @LogAnnotation(value="查询收款单列表")
    @JsonConvert(
            type = FinanceReceivable.class,
            includes = {"id","code", "orderMain", "description","payAmount","transactionCode"
            ,"payChannel", "payType","payStatus","user","createTime"},
            nest = {
                    @NestConvert(type = OrderMain.class, includes = {"id", "code"}),
                    @NestConvert(type = User.class, includes = {"id", "username"})
            })
    public Result findAll(@RequestBody FinanceReceivableSearch financeReceivableSearch) {
        Page<FinanceReceivable> orders = financeReceivableService.findAll(financeReceivableSearch);
        return ResultUtil.success(orders);
    }
    


}
