package com.delllogistics.repository.order;

import com.delllogistics.entity.enums.OrderMainStatus;
import com.delllogistics.entity.order.OrderMain;
import com.delllogistics.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMainRepository
        extends PagingAndSortingRepository<OrderMain, Long>, JpaSpecificationExecutor<OrderMain> {

    int countAllByUserAndCompany_idAndStatusAndIsDeleted(User user,Long companyId, OrderMainStatus status, boolean isDeleted);

    @Query(value="SELECT count(t.counts) " +
            "FROM (SELECT count(i.order_main_id) counts FROM order_main m " +
            "                INNER JOIN order_item i ON m.id = i.order_main_id " +
            "                INNER JOIN user u ON m.user_id = u.id " +
            "        WHERE m.status = ? " +
            "              AND (i.order_item_evaluate_status   IS NULL OR i.order_item_evaluate_status <> ?) " +
            "              AND m.is_deleted = 0 AND u.id = ? AND m.company_id=? GROUP BY i.order_main_id, m.id) t " ,nativeQuery = true)
    int countByUserAndStatusAndOrderItems(String orderMainStatus, String orderItemEvaluateStatus,Long userId,Long companyId);

    @Query(value = "SELECT  * FROM  order_main m " +
            " WHERE   DATE_ADD(m.delivery_time,INTERVAL m.day_num DAY) <= now()  " +
            "AND m.order_after_sales_status IS  NULL AND m.status = ? AND  m.user_id=? 1 /*#pageable*/ ",
            nativeQuery = true)
    Page<OrderMain> findRentOrderByRent( String status  ,Long userId, Pageable pageable);


    @Query(value = "SELECT  * FROM  order_main m " +
            " WHERE   DATE_ADD(m.delivery_time,INTERVAL m.day_num DAY) <= now()  " +
            "AND m.order_after_sales_status IS  NULL AND m.status = ? AND  m.user_id=? 1 /*#pageable*/ ",
            nativeQuery = true)
    Page<OrderMain> findRentOrderWaitSurrender( String status  ,Long userId, Pageable pageable);

    List<OrderMain> findAllByIsDeletedAndStatus(boolean isDeleted,OrderMainStatus status);


    OrderMain findByIdAndIsDeleted(Long id, boolean isDeleted);


}