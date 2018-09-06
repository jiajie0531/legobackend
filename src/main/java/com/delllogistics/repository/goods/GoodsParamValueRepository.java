package com.delllogistics.repository.goods;

import com.delllogistics.entity.goods.GoodsParamValue;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *  商品参数值可选范围
 * Created by calvin  2018/3/19
 */
@Repository
public interface GoodsParamValueRepository extends PagingAndSortingRepository<GoodsParamValue, Long>, JpaSpecificationExecutor<GoodsParamValue> {

    List<GoodsParamValue> findAllByGoodsParam_Id(Long goodsParamId);
}