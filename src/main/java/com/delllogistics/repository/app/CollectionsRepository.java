package com.delllogistics.repository.app;

import com.delllogistics.entity.app.Collections;
import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


/**
 * 收藏Repo.<br/>
 * User: jiajie<br/>
 * Date: 09/12/2017<br/>
 * Time: 10:37 PM<br/>
 */
@Repository
public interface CollectionsRepository
        extends PagingAndSortingRepository<Collections, Long>, JpaSpecificationExecutor<Collections> {

    Page<Collections> findAllByUserAndCompany_idAndIsDeleted(User user, Long companyId, boolean isDeleted, Pageable pageable);

    Integer countByUser_idAndCompany_idAndIsDeleted(Long userId, Long companyId, boolean isDeleted);

    Integer countByUserAndGoodsAndIsDeleted(User user, Goods goods, boolean isDeleted);

    Collections findByUserAndIsDeletedAndGoods(User user, boolean isDeleted, Goods goods);

    Collections findByUserAndGoods(User user, Goods goods);

}
