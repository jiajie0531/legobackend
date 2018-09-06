package com.delllogistics.repository.goods;

import com.delllogistics.entity.goods.GoodsBrand;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


/**
 * Created by jiajie on 27/10/2017.
 */
@Repository
public interface GoodsBrandRepository
        extends PagingAndSortingRepository<GoodsBrand, Long>, JpaSpecificationExecutor<GoodsBrand> {


}
