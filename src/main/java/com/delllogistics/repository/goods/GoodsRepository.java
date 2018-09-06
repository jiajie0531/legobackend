package com.delllogistics.repository.goods;

import com.delllogistics.entity.goods.Goods;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;


/**
 * Created by jiajie on 27/10/2017.
 */
@Repository
public interface GoodsRepository
        extends PagingAndSortingRepository<Goods, Long>, JpaSpecificationExecutor<Goods> {


    Set<Goods> findAllByIsDeletedAndCompany_IdInOrderByName(boolean isDeleted,Set<Long> companies);

}