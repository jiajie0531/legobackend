package com.delllogistics.security;

import com.delllogistics.entity.user.User;
import com.delllogistics.exception.BizExceptionEnum;
import com.delllogistics.exception.JwtAuthException;
import com.delllogistics.repository.user.UserRepository;
import com.delllogistics.util.JwtTokenUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

/** TOken 验证
 * Created by xzm on 07/05/2017.
 */
@Service
public class TokenAuthenticationUserDetailsService
        implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private final UserRepository userRepository;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public TokenAuthenticationUserDetailsService(UserRepository userRepository, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token)  {
        String principal = (String) token.getPrincipal();
        if (principal != null && principal.startsWith("Bearer ")) {
            /*
            验证token是否过期,包含了验证jwt是否正确
             */
            String authToken = principal.substring(7);
            try{
                boolean flag = jwtTokenUtil.isTokenExpired(authToken);
                if(flag){
                /*
                token过期
                 */
                    throw new JwtAuthException(BizExceptionEnum.TOKEN_EXPIRED);
                }else{

                    User user = userRepository.findByToken(authToken);

                    if (user==null) {
                        throw new JwtAuthException(BizExceptionEnum.TOKEN_AUTH_ERROR);
                    } else {
                        return new TokenUserDetails(user);
                    }
                }
            }catch (JwtException e){
                throw new JwtAuthException(BizExceptionEnum.TOKEN_ERROR);
            }

        }else{
            /*
            token格式错误
             */
            throw new JwtAuthException(BizExceptionEnum.TOKEN_ERROR);
        }

    }
}
