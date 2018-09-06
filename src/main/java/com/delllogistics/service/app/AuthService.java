package com.delllogistics.service.app;

import com.delllogistics.config.properties.RestProperties;
import com.delllogistics.dto.app.AuthRequest;
import com.delllogistics.dto.app.AuthResponse;
import com.delllogistics.entity.enums.UserType;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.user.UserAccount;
import com.delllogistics.exception.BizExceptionEnum;
import com.delllogistics.exception.GeneralException;
import com.delllogistics.repository.user.UserAccountRepository;
import com.delllogistics.repository.user.UserRepository;
import com.delllogistics.util.JwtTokenUtil;
import com.delllogistics.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


/**登录验证
 * Created by xzm on 2018-1-26.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;

    private final JwtTokenUtil jwtTokenUtil;

    private final RestProperties restProperties;

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public AuthService(UserRepository userRepository, JwtTokenUtil jwtTokenUtil, RestProperties restProperties, UserAccountRepository userAccountRepository) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.restProperties = restProperties;
        this.userAccountRepository = userAccountRepository;
    }
    public AuthResponse authUser(AuthRequest authRequest){
        String username = authRequest.getUsername();
        String password = authRequest.getPassword();
        if(!StringUtils.isEmpty(username)&& !StringUtils.isEmpty(password)){
            User user = userRepository.findByUsernameAndType(username, UserType.WECHATUSER.value);
            if(user!=null){
                if(MD5Util.encrypt(password).equals(user.getPassword())){
                    Company company = user.getCompany();
                    if(company==null){
                        throw new GeneralException(BizExceptionEnum.NO_User_COMPANY);
                    }
                    final String randomKey = jwtTokenUtil.getRandomKey();
                    final String token = jwtTokenUtil.generateToken(username, randomKey);
                    String oldToken = user.getToken();
                    UserAccount userAccount = userAccountRepository.findByUser_idAndCompany_idAndIsDeleted(user.getId(), company.getId(), false);
                    if(StringUtils.isEmpty(oldToken)){
                        user.setToken(token);
                        userRepository.save(user);
                    }else if(jwtTokenUtil.isTokenExpired(oldToken)){
                        user.setToken(token);
                        userRepository.save(user);
                    }else if(restProperties.isSsoOpen()){
                        user.setToken(token);
                        userRepository.save(user);
                    }else{
                        return new AuthResponse(oldToken, jwtTokenUtil.getMd5KeyFromToken(oldToken),userAccount);
                    }
                    return new AuthResponse(token, randomKey, userAccount);
                }
            }
        }
        throw new GeneralException(BizExceptionEnum.AUTH_REQUEST_ERROR);
    }
}
