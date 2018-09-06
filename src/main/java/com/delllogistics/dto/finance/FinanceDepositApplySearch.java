package com.delllogistics.dto.finance;

import com.delllogistics.dto.BaseSearchModel;
import com.delllogistics.entity.enums.ApplyStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Getter
@Setter
public class FinanceDepositApplySearch extends BaseSearchModel {


    /**
     * 用户状态
     */
    @Enumerated(EnumType.STRING)
    private ApplyStatus applyStatus;


}
