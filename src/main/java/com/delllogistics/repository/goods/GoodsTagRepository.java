package com.delllogistics.repository.goods;

import com.delllogistics.entity.goods.GoodsTag;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsTagRepository
        extends PagingAndSortingRepository<GoodsTag, Long>, JpaSpecificationExecutor<GoodsTag> {

    GoodsTag findByName(String name);


}
