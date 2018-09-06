package com.delllogistics.repository.goods;

import com.delllogistics.entity.goods.GoodsParam;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *  商品参数配置
 * Created by calvin  2018/3/19
 */
@Repository
public interface GoodsParamRepository extends PagingAndSortingRepository<GoodsParam, Long>, JpaSpecificationExecutor<GoodsParam> {





}