package com.delllogistics.repository.app;

import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.goods.GoodsDetail;
import com.delllogistics.entity.app.ShoppingCart;
import com.delllogistics.entity.user.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartRepository
        extends PagingAndSortingRepository<ShoppingCart,Long>, JpaSpecificationExecutor<ShoppingCart> {

    List<ShoppingCart> findAllByUserAndCompany_idAndIsDeleted(User user,Long companyId, boolean isDeleted);


    ShoppingCart findByUserAndGoodsAndGoodsDetailAndIsDeleted(User user, Goods goods, GoodsDetail goodsDetail, boolean isDeleted);

    ShoppingCart findByUserAndGoodsAndIsDeleted(User user, Goods goods, boolean isDeleted);
}
