package com.delllogistics.repository.user;

import com.delllogistics.entity.user.MembershipPromotionLog;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * 会员推广返利.<br/>
 * User: jiajie<br/>
 * Date: 11/03/2018<br/>
 * Time: 3:01 PM<br/>
 */
@Repository
public interface MembershipPromotionRepository
        extends PagingAndSortingRepository<MembershipPromotionLog,Long>,JpaSpecificationExecutor<MembershipPromotionLog> {
}
