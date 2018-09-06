package com.delllogistics.repository.order;

import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.order.OrderItem;
import com.delllogistics.entity.order.OrderMain;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository
        extends PagingAndSortingRepository<OrderItem, Long>, JpaSpecificationExecutor<OrderItem> {

    OrderItem findByOrderMainAndAndGoodsAndIsDeleted(OrderMain orderMain, Goods goods, boolean isDeleted);

    @Query(value = "SELECT oi.goods_id, sum(quantity) AS sumQuantity FROM order_item oi where oi.create_time >= ?1 GROUP BY oi.goods_id ORDER BY sum(quantity) DESC LIMIT 0, 10", nativeQuery=true)
    Object[][] findSellCount(String startedTime);


    List<OrderItem> findByIdIn(Long[] orderItemids);



}
