package com.delllogistics.repository.goods;

import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.goods.GoodsDetail;
import com.delllogistics.entity.sys.PhoneValidateCode;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


@Repository
public interface GoodsDetailRepository extends PagingAndSortingRepository<GoodsDetail, Long>, JpaSpecificationExecutor<GoodsDetail> {

    Set<GoodsDetail> findByGoodsAndIsDeleted(Goods goods, boolean isDeleted);

    List<GoodsDetail> findByGoods_Id(Long goodsId);

    @Query(value="select sum(stock) from goods_detail where goods_id=? " ,nativeQuery = true)
    BigDecimal  getSumStockByGoodsId(Long goodsId);




 /*   @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE  GoodsDetail gd  SET gd.sort = :sort WHERE ad.user_id =? AND ad.id !=? ",nativeQuery = true)
    int updateGoodsDetail(@Param("goods") Goods name,);
*/




}
