package com.delllogistics.repository.user;

import com.delllogistics.entity.user.UserAccount;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends PagingAndSortingRepository<UserAccount, Long>,JpaSpecificationExecutor<UserAccount> {

    UserAccount findByUser_idAndCompany_idAndIsDeleted(Long userId,Long companyId,boolean isDeleted);
}
