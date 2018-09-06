package com.delllogistics.controller.finance;

import com.delllogistics.dto.Result;
import com.delllogistics.dto.finance.FinanceDepositApplySearch;
import com.delllogistics.entity.Finance.FinanceDepositApply;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.user.UserAccount;
import com.delllogistics.service.finance.FinanceDepositApplyService;
import com.delllogistics.spring.annotation.*;
import com.delllogistics.util.ResultUtil;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestJsonController
@RequestMapping("/financeDepositApply")
public class FinanceDepositApplyController {

    private final FinanceDepositApplyService financeDepositApplyService;

    public FinanceDepositApplyController(FinanceDepositApplyService financeDepositApplyService) {
        this.financeDepositApplyService = financeDepositApplyService;
    }


    /**
     * 查询预存款申请查询列表
     * @param financeDepositApplySearch 预存款申请查询
     * @return 预存款申请列表
     */
    @PostMapping("/findAll")
    @LogAnnotation(value="查询预存款申请")
    @JsonConvert(
            type = FinanceDepositApply.class,
            includes = {"id","amount","code","applyStatus" ,"createTime","userAccount","actionUser"},
            nest = {
                    @NestConvert(type = User.class, includes = {"id", "username","photo"}),
                    @NestConvert(type = UserAccount.class, includes = {"id", "user"})
            })
    public Result findAll(@RequestBody FinanceDepositApplySearch financeDepositApplySearch) {
        Page<FinanceDepositApply> financeDepositApplies = financeDepositApplyService.findAll(financeDepositApplySearch);
        return ResultUtil.success(financeDepositApplies);
    }

    /**
     * 保存预存款申请
     * @param financeDepositApply 预存款申请
     * @param user 用户
     * @return 返回结果
     */
    @LogAnnotation(value="保存预存款申请")
    @PostMapping("/save")
    public Result save(@RequestBody FinanceDepositApply financeDepositApply , @CurrentUser User user){
        financeDepositApplyService.save(financeDepositApply,user);
        return ResultUtil.success();
    }

    /**
     * 通过预存款申请
     * @param financeDepositApply 预存款申请
     * @param user 用户
     * @return 返回结果
     */
    @PostMapping("/success")
    @LogAnnotation(value="通过预存款申请")
    public Result success(@RequestBody FinanceDepositApply financeDepositApply , @CurrentUser User user){
        financeDepositApplyService.success(financeDepositApply,user);
        return ResultUtil.success();
    }

    /**
     * 驳回预存款申请
     * @param financeDepositApply 预存款申请信息
     * @param user 用户
     * @return 返回结果
     */
    @PostMapping("/failed")
    @LogAnnotation(value="驳回预存款申请")
    public Result failed(@RequestBody FinanceDepositApply financeDepositApply , @CurrentUser User user){
        financeDepositApplyService.failed(financeDepositApply,user);
        return ResultUtil.success();
    }

}
