package com.delllogistics.controller.finance;

import com.delllogistics.dto.Result;
import com.delllogistics.dto.finance.FinanceDepositApplyLogSearch;
import com.delllogistics.entity.Finance.FinanceDepositApplyLog;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.user.UserAccount;
import com.delllogistics.service.finance.FinanceDepositApplyLogService;
import com.delllogistics.spring.annotation.*;
import com.delllogistics.util.ResultUtil;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestJsonController
@RequestMapping("/financeDepositApplyLog")
public class FinanceDepositApplyLogController {

    private final FinanceDepositApplyLogService financeDepositApplyLogService;

    public FinanceDepositApplyLogController(FinanceDepositApplyLogService financeDepositApplyLogService) {
        this.financeDepositApplyLogService = financeDepositApplyLogService;
    }

    /**
     * 查询预存款申请日志列表
     * @param financeDepositApplyLogSearch 预存款申请日志查询
     * @return 返回结果集
     */
    @PostMapping("/findAll")
    @JsonConvert(
            type = FinanceDepositApplyLog.class,
            includes = {"id","amount","applyStatus" ,"createTime","userAccount","actionUser"},
            nest = {
                    @NestConvert(type = User.class, includes = {"id", "username","photo"}),
                    @NestConvert(type = UserAccount.class, includes = {"id", "user"})
            })
    @LogAnnotation(value="查询预存款申请日志列表")
    public Result findAll(@RequestBody FinanceDepositApplyLogSearch financeDepositApplyLogSearch) {
        Page<FinanceDepositApplyLog> financeDepositApplies = financeDepositApplyLogService.findAll(financeDepositApplyLogSearch);
        return ResultUtil.success(financeDepositApplies);
    }

}
