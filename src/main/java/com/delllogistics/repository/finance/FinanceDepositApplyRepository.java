package com.delllogistics.repository.finance;

import com.delllogistics.entity.Finance.FinanceDepositApply;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanceDepositApplyRepository
        extends PagingAndSortingRepository<FinanceDepositApply, Long>, JpaSpecificationExecutor<FinanceDepositApply> {

}

