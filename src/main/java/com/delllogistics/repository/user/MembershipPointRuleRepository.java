package com.delllogistics.repository.user;

import com.delllogistics.entity.enums.MembershipPointType;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.user.MembershipPointRule;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembershipPointRuleRepository extends PagingAndSortingRepository<MembershipPointRule,Long>,JpaSpecificationExecutor<MembershipPointRule> {

    MembershipPointRule findByMembershipPointTypeAndCompanyAndIsDeleted(MembershipPointType membershipPointType, Company company, boolean isDeleted);

    List<MembershipPointRule> findAllByCompanyAndIsDeleted(Company company, boolean isDeleted);
}
