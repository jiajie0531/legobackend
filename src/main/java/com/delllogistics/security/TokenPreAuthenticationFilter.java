package com.delllogistics.security;

import com.delllogistics.config.properties.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

/** jwt  token验证
 * Created by xzm on 07/05/2017.
 */
public class TokenPreAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    public static final String SSO_TOKEN="X-Auth-Token";

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    public TokenPreAuthenticationFilter(AuthenticationManager authenticationManager) {
        setAuthenticationManager(authenticationManager);
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return request.getHeader(jwtProperties.getHeader());


    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return jwtProperties.getSSO_CREDENTIALS();
    }
}
