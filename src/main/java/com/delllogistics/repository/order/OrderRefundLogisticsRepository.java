package com.delllogistics.repository.order;

import com.delllogistics.entity.order.OrderRefundLogistics;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRefundLogisticsRepository
        extends PagingAndSortingRepository<OrderRefundLogistics, Long>, JpaSpecificationExecutor<OrderRefundLogistics> {

}

