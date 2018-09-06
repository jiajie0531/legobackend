package com.delllogistics.repository.sys;

import com.delllogistics.entity.sys.SysGoodsCategory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


/**
 *  系统商品分类
 * Created by calvin  2018/3/21
 */
@Repository
public interface SysGoodsCategoryRepository
        extends PagingAndSortingRepository<SysGoodsCategory, Long>, JpaSpecificationExecutor<SysGoodsCategory> {


}
