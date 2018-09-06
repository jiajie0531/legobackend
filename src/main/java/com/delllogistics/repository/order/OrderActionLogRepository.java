package com.delllogistics.repository.order;

import com.delllogistics.entity.order.OrderActionLog;
import com.delllogistics.entity.enums.OrderActionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderActionLogRepository
        extends PagingAndSortingRepository<OrderActionLog, Long>, JpaSpecificationExecutor<OrderActionLog> {

    Page<OrderActionLog> findByOrderMain_Id(Long orderMainId , Pageable pageable);

    Page<OrderActionLog> findByOrderMain_IdAndOrderActionType(Long orderMainId  , OrderActionType orderActionType, Pageable pageable);

}

