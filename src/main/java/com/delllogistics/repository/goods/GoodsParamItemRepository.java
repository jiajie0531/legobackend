package com.delllogistics.repository.goods;

import com.delllogistics.entity.goods.GoodsParamItem;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *  商品参数值
 * Created by calvin  2018/3/19
 */
@Repository
public interface GoodsParamItemRepository extends PagingAndSortingRepository<GoodsParamItem, Long>, JpaSpecificationExecutor<GoodsParamItem> {

    GoodsParamItem findByGoods_IdAndGoodsParam_Id(Long goodsId,Long goodsParamId);
}