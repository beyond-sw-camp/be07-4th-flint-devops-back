package com.hotel.flint.common.configs;

import com.hotel.flint.common.auth.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity // security code 임을 선언하는 annotation
@EnableGlobalMethodSecurity(prePostEnabled = true) // pre: 사전 검증, post: 사후 검증
public class SecurityConfigs {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf().disable()
                .cors().and()
                .httpBasic().disable()
                .authorizeRequests()
                    .antMatchers("/member/signup","/member/signup/verified",
                            "/*/findemail", "/", "/*/login", "/sendEmail",
                            "/*/findpassword","/submit", "/subscribe")
                    .permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }
}
