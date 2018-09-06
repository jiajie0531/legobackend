package com.delllogistics.repository.order;

import com.delllogistics.entity.order.OrderServiceEvaluate;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderServiceEvaluateRepository extends PagingAndSortingRepository<OrderServiceEvaluate, Long>, JpaSpecificationExecutor<OrderServiceEvaluate> {

}
