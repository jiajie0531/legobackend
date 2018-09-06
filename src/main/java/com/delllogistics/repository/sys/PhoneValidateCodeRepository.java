package com.delllogistics.repository.sys;

import com.delllogistics.entity.sys.PhoneValidateCode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jiajie on 10/06/2017.
 */
@Repository
public interface PhoneValidateCodeRepository extends CrudRepository<PhoneValidateCode, Long> {
    PhoneValidateCode findByPhoneAndCode(String phone, int code);

    /**
     * 按手机号码查询最近一次的验证码
     * @param phone
     * @return
     */
    @Query(value="select * from phone_validate_code where phone=? order by id desc limit 1" ,nativeQuery = true)
    PhoneValidateCode findByPhoneAndMaxId(String phone);
}
