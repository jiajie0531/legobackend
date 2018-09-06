package com.delllogistics.repository.finance;

import com.delllogistics.entity.Finance.FinanceReceivable;
import com.delllogistics.entity.enums.PayStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanceReceivableRepository
        extends PagingAndSortingRepository<FinanceReceivable, Long>, JpaSpecificationExecutor<FinanceReceivable> {

    /**
     * 根据订单ID和支付状态查询订单的收款单号
     * @param orderMainId 订单ID
     * @param payStatus 支付状态
     * @return
     */
    FinanceReceivable findByOrderMain_IdAndPayStatus(Long orderMainId, PayStatus payStatus);
}

