package com.delllogistics.controller.finance;

import com.delllogistics.dto.Result;
import com.delllogistics.dto.finance.FinanceDepositSearch;
import com.delllogistics.entity.Finance.FinanceDeposit;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.user.UserAccount;
import com.delllogistics.service.finance.FinanceDepositService;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.NestConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import com.delllogistics.util.ResultUtil;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestJsonController
@RequestMapping("/financeDeposit")
public class FinanceDepositController {

    private final FinanceDepositService financeDepositService;

    public FinanceDepositController(FinanceDepositService financeDepositService) {
        this.financeDepositService = financeDepositService;
    }


    /**
     * 查询预存款列表
     * @param financeDepositSearch 预存款搜索信息
     * @return 订单列表
     */
    @PostMapping("/findAll")
    @JsonConvert(
            type = UserAccount.class,
            includes = {"id", "user","financeDeposit","createTime" },
            nest = {
                    @NestConvert(type = FinanceDeposit.class, includes = {"id","usableAmount","frozenAmount","createTime"}),
                    @NestConvert(type = User.class, includes = {"id","username","photo","userStatus"}),
            })
    public Result findAll(@RequestBody FinanceDepositSearch financeDepositSearch) {
        Page<UserAccount> users = financeDepositService.findAll(financeDepositSearch);
        return ResultUtil.success(users);
    }



}
