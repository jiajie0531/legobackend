package com.delllogistics.config;

import com.delllogistics.config.properties.JwtProperties;
import com.delllogistics.security.RestAuthenticationEntryPoint;
import com.delllogistics.security.TokenAuthenticationUserDetailsService;
import com.delllogistics.security.TokenPreAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;

/**
 * Created by xzm on 2017/5/23.
 * Spring Security权限配置
 */
@Component
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    private final TokenAuthenticationUserDetailsService tokenAuthenticationUserDetailsService;
    private final JwtProperties jwtProperties;


    @Autowired
    public SecurityConfig(TokenAuthenticationUserDetailsService tokenAuthenticationUserDetailsService, JwtProperties jwtProperties) {
        this.tokenAuthenticationUserDetailsService = tokenAuthenticationUserDetailsService;
        this.jwtProperties=jwtProperties;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                csrf().disable().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().
                authorizeRequests().
                requestMatchers(CorsUtils::isPreFlightRequest).permitAll().
                antMatchers(
                        "/druid/**",
                        "/user/getUserByWechat",
                        "/favicon.ico",
                        "/img/**",
                        "/backendUser/login",
//                        "/sysFile/uploadGoodsEditorImg",
                        "/app/search/**",
                        "/app/goods/findById",
                        "/app/goodsComment/**",
                        "/app/home/**",
                        "/app/hello/**",
                        "/gs-guide-websocket/**",
                        "/gs-guide-websocket/info",
                        "/index.html",
                        "/app.js",
                        "/main.css",
                        "/orderItem/**",
                        "/appOrder/resultnotify",
                        "/orderRefund/resultnotify",
                        jwtProperties.getAuthPath()

                ).permitAll().
                antMatchers("/","/error","/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**", "/swagger-resources/configuration/ui", "/swagger-resources/configuration/security").permitAll().
                anyRequest().authenticated().
                and().
                exceptionHandling().
                authenticationEntryPoint(restAuthenticationEntryPoint());
        http.addFilter(headerAuthenticationFilter());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.authenticationProvider(preAuthenticationProvider());
    }

    private AuthenticationProvider preAuthenticationProvider() {
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(tokenAuthenticationUserDetailsService);

        return provider;
    }


    @Bean
    public TokenPreAuthenticationFilter headerAuthenticationFilter() throws Exception {
        return new TokenPreAuthenticationFilter(authenticationManager());
    }

    public AuthenticationEntryPoint restAuthenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }
}
