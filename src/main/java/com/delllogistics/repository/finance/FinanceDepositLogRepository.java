package com.delllogistics.repository.finance;

import com.delllogistics.entity.Finance.FinanceDepositLog;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanceDepositLogRepository
        extends PagingAndSortingRepository<FinanceDepositLog, Long>, JpaSpecificationExecutor<FinanceDepositLog> {

}

