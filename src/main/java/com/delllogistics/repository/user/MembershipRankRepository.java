package com.delllogistics.repository.user;

import com.delllogistics.entity.user.MembershipRank;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * 用户等级
 * Created by calvin  2017/12/9
 */
@Repository
public interface MembershipRankRepository
        extends PagingAndSortingRepository<MembershipRank, Long>, JpaSpecificationExecutor<MembershipRank> {


    MembershipRank findByMinPointsLessThanEqualAndMaxPointsGreaterThanEqualAndCompany_Id(int minPoints, int maxPoints,Long companyId);
}
