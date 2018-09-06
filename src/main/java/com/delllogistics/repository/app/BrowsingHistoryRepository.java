package com.delllogistics.repository.app;

import com.delllogistics.entity.app.BrowsingHistory;
import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 足迹Repo.<br/>
 * User: jiajie<br/>
 * Date: 09/12/2017<br/>
 * Time: 10:39 PM<br/>
 */
@Repository
public interface BrowsingHistoryRepository
        extends PagingAndSortingRepository<BrowsingHistory, Long>, JpaSpecificationExecutor<BrowsingHistory> {

    Page<BrowsingHistory> findAllByUserAndCompany_idAndIsDeleted(User user,Long companyId, boolean isDeleted, Pageable pageable);

    Integer countByUser_idAndCompany_id(Long userId, Long companyId);

    BrowsingHistory findByGoodsAndUserAndIsDeleted(Goods goods,User user,boolean isDeleted);

}
