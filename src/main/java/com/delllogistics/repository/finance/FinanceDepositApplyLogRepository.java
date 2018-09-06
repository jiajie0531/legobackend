package com.delllogistics.repository.finance;

import com.delllogistics.entity.Finance.FinanceDepositApplyLog;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanceDepositApplyLogRepository
        extends PagingAndSortingRepository<FinanceDepositApplyLog, Long>, JpaSpecificationExecutor<FinanceDepositApplyLog> {

}

