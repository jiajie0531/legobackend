package com.delllogistics.repository.goods;

import com.delllogistics.entity.goods.GoodsSeries;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;


/**
 * Created by jiajie on 27/10/2017.
 */
@Repository
public interface GoodsSeriesRepository
        extends PagingAndSortingRepository<GoodsSeries, Long>, JpaSpecificationExecutor<GoodsSeries> {

        Set<GoodsSeries> findByIdIn(Long[] id);

}
