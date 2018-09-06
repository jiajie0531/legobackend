package com.delllogistics.repository.user;

import com.delllogistics.entity.user.User;
import com.delllogistics.entity.user.WechatUser;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long>,JpaSpecificationExecutor<User> {

    User findByUsernameAndType(String username,int type);

    User findByToken(String token);

    User findByWechatUser(WechatUser wechatUser);

}
