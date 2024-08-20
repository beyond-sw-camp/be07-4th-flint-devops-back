package com.hotel.flint.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class JwtAuthFilter extends GenericFilter {

    @Value("${jwt.secretKey}")
    private String secretKey;

    private final HttpServletResponse httpServletResponse;

    public JwtAuthFilter(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String bearerToken = ((HttpServletRequest) servletRequest).getHeader("Authorization");
        try {
            if(bearerToken!= null){
                if(!bearerToken.substring(0,7).equals("Bearer ")){
                    throw new AuthenticationServiceException("Bearer 형식이 아닙니다.");
                }
                String token = bearerToken.substring(7);
                Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

                List<GrantedAuthority> authorities = new ArrayList<>();
                if (claims.containsKey("department")) {
                    String department = (String) claims.get("department");
                    authorities.add(new SimpleGrantedAuthority("DEPARTMENT_" + department));
                }

                UserDetails userDetails = new User(claims.getSubject(), "", authorities);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e){
            log.error(e.getMessage());
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write("로그인을 다시 진행해주세요");
        }
    }
}
