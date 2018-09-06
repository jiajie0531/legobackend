package com.delllogistics.dto.finance;

import com.delllogistics.dto.BaseSearchModel;
import com.delllogistics.entity.Finance.FinanceDepositApply;
import com.delllogistics.entity.enums.ApplyStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class FinanceDepositApplyLogSearch  extends BaseSearchModel{


    /**
     * 用户状态
     */
    @Enumerated(EnumType.STRING)
    private ApplyStatus applyStatus;

    /**
     * 预存款申请单
     */
    @Enumerated(EnumType.STRING)
    private FinanceDepositApply financeDepositApply;

}
