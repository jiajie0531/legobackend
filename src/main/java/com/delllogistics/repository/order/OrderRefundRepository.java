package com.delllogistics.repository.order;

import com.delllogistics.entity.order.OrderRefund;
import com.delllogistics.entity.enums.OrderRefundStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRefundRepository
        extends PagingAndSortingRepository<OrderRefund, Long>, JpaSpecificationExecutor<OrderRefund> {


    OrderRefund findByOrderRefundStatusAndOrderItem_Id(OrderRefundStatus orderRefundStatus, Long orderItemId);

    OrderRefund findByOrderItem_Id(Long orderItemId);




    @Query(value = "SELECT ifnull(sum(refund_amount), 0) as sum_refund_amount FROM order_refund orf WHERE user_id = ?1", nativeQuery = true)
    String getSumRefundAmount(Long userId);
}

