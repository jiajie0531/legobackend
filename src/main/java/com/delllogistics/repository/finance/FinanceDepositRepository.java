package com.delllogistics.repository.finance;

import com.delllogistics.entity.Finance.FinanceDeposit;
import com.delllogistics.entity.user.UserAccount;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanceDepositRepository
        extends PagingAndSortingRepository<FinanceDeposit, Long>, JpaSpecificationExecutor<FinanceDeposit> {

    FinanceDeposit findByUserAccount(UserAccount userAccount);
    FinanceDeposit findByUserAccount_Id(Long userAccountId);
}

