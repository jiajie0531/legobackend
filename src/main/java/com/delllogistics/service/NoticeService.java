package com.delllogistics.service;

import com.delllogistics.client.notice.NoticeClient;
import com.delllogistics.repository.sys.PhoneValidateCodeRepository;
import com.delllogistics.entity.sys.PhoneValidateCode;
import com.delllogistics.tool.Language;
import com.delllogistics.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by jiajie on 10/06/2017.
 */
@Service
public class NoticeService {

    private static final int MIN_CODE = 100000;
    private static final int MAX_CODE = 999999;
    private static final String CODE_TEMPLATE_ID_CN = "k7bzg1";
    private static final String CODE_TEMPLATE_ID_EN = "k7bzg1";

    private final NoticeClient noticeClient;

    private final PhoneValidateCodeRepository validateCodeDao;

    @Value("${notice.message.expireTime}")
    private int expireTime;

    @Autowired
    public NoticeService(NoticeClient noticeClient, PhoneValidateCodeRepository validateCodeDao) {
        this.noticeClient = noticeClient;
        this.validateCodeDao = validateCodeDao;
    }

    public int sendCode(com.delllogistics.dto.PhoneValidateCode phoneValidateCode) {
        if (phoneValidateCode == null || StringUtils.isEmpty(phoneValidateCode.getPhone())) {
            return 0;
        }
        int code = MIN_CODE + new Random().nextInt(MAX_CODE - MIN_CODE);
        PhoneValidateCode validateCode = new PhoneValidateCode();
        validateCode.setPhone(phoneValidateCode.getPhone());
        validateCode.setCode(code);
        validateCodeDao.save(validateCode);

        String templateId = Language.isSimplifiedChinese() ? CODE_TEMPLATE_ID_CN : CODE_TEMPLATE_ID_EN;
        noticeClient.sendMessage(templateId, phoneValidateCode.getPhone(), new HashMap<String, String>() {
            {
                put("code", String.valueOf(code));
            }
        });

        return code;
    }

    public boolean validateCode(String phone, int code) {
        PhoneValidateCode validateCode = validateCodeDao.findByPhoneAndMaxId(phone);

        /*
          比较最近一次的验证码是否相等且未使用过，防止验证码有效期内多个验证码可用
         */
        if (validateCode != null && code == validateCode.getCode() && !validateCode.isUsed()) {
            validateCode.setUsed(true);
            Date updateTime = validateCode.getUpdateTime();
            /*
              无论验证码是否过期都更新为已用
             */
            validateCodeDao.save(validateCode);
            return DateUtils.compareDateByExpire(updateTime, expireTime);
        } else {
            return false;
        }
    }


}
