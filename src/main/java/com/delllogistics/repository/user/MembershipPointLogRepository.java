package com.delllogistics.repository.user;

import com.delllogistics.dto.user.UserPoints;
import com.delllogistics.entity.user.MembershipPointLog;
import com.delllogistics.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 会员积分
 */
@Repository
public interface MembershipPointLogRepository
        extends PagingAndSortingRepository<MembershipPointLog, Long>, JpaSpecificationExecutor<MembershipPointLog> {

    Page<MembershipPointLog> findAllByUser(User user, Pageable pageable);

    @Query(value = "SELECT new com.delllogistics.dto.user.UserPoints(m.membershipPointType, sum(m.points) ) FROM MembershipPointLog m WHERE m.user.id = ?1 and m.company.id=?2 GROUP BY m.membershipPointType")
    List<UserPoints> findPoints(Long userId,Long companyId);
}
