package com.delllogistics.repository.user;

import com.delllogistics.entity.user.User;
import com.delllogistics.entity.app.UserAddress;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 *  用户收货地址
 * Created by calvin  2017/12/9
 */
@Repository
public interface UserAddressRepository extends PagingAndSortingRepository<UserAddress,Long>,JpaSpecificationExecutor<UserAddress> {


    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE  user_address ad  SET ad.is_used = 1 WHERE ad.user_id =? AND ad.id =? ",nativeQuery = true)
    int isUsed( Long userId,  Long id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE  user_address ad  SET ad.is_used = 0 WHERE ad.user_id =? AND ad.id !=? ",nativeQuery = true)
    int isOtherNoneUsed( Long userId,  Long id);

    UserAddress findByUserAndIsUsedAndIsDeleted(User user,boolean isUsed,boolean isDeleted);
}
