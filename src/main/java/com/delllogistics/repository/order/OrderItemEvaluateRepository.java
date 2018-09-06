package com.delllogistics.repository.order;

import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.order.OrderItem;
import com.delllogistics.entity.order.OrderItemEvaluate;
import com.delllogistics.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderItemEvaluateRepository extends PagingAndSortingRepository<OrderItemEvaluate, Long>, JpaSpecificationExecutor<OrderItemEvaluate> {

    OrderItemEvaluate  findByUserAndGoodsAndOrderItem(User user , Goods goods, OrderItem orderItem );

    Page<OrderItemEvaluate> findOrderItemEvaluatesByGoods(Goods goods, Pageable pageable);
}
