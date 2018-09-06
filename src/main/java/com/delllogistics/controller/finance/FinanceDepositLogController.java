package com.delllogistics.controller.finance;

import com.delllogistics.dto.Result;
import com.delllogistics.dto.finance.FinanceDepositLogSearch;
import com.delllogistics.entity.Finance.FinanceDepositLog;
import com.delllogistics.entity.order.OrderMain;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.user.UserAccount;
import com.delllogistics.service.finance.FinanceDepositLogService;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.NestConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import com.delllogistics.util.ResultUtil;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestJsonController
@RequestMapping("/financeDepositLog")
public class FinanceDepositLogController {

    private final FinanceDepositLogService financeDepositLogService;

    public FinanceDepositLogController(FinanceDepositLogService financeDepositLogService) {
        this.financeDepositLogService = financeDepositLogService;
    }

    /**
     * 查询预存款明细列表
     * @param financeDepositLogSearch 预存款明细搜索信息
     * @return 订单列表
     */
    @PostMapping("/findAll")
    @JsonConvert(
            type = FinanceDepositLog.class,
            includes = {"id","code","balance","amount", "orderMain", "description","transactionCode"
            ,"payChannel", "payType","payStatus","userAccount","createTime"},
            nest = {
                    @NestConvert(type = OrderMain.class, includes = {"id", "code"}),
                    @NestConvert(type = UserAccount.class, includes = {"id", "user"}),
                    @NestConvert(type = User.class, includes = {"id", "username"})
            })
    public Result findAll(@RequestBody FinanceDepositLogSearch financeDepositLogSearch) {
        Page<FinanceDepositLog>  financeDepositLogs = financeDepositLogService.findAll(financeDepositLogSearch);
        return ResultUtil.success(financeDepositLogs);
    }
    


}
