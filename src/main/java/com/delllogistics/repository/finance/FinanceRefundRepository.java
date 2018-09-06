package com.delllogistics.repository.finance;

import com.delllogistics.entity.Finance.FinanceRefund;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanceRefundRepository
        extends PagingAndSortingRepository<FinanceRefund, Long>, JpaSpecificationExecutor<FinanceRefund> {

}

