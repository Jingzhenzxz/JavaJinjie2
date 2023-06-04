package com.wuan.attendance.config;

import com.wuan.attendance.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
public class JwtFilter extends GenericFilterBean {
    private static final String AUTH_TOKEN_KEY = "Authorization";
    private final JwtUtil jwtUtil;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String requestURI = httpRequest.getRequestURI();
        // Ignore login requests
        if (requestURI.contains("/api/authentication/login")) {
            chain.doFilter(request, response);
            return;
        }

        // 从请求中获取 JWT
        String authToken = getAuthToken(httpRequest);
        if (authToken != null && jwtUtil.validateToken(authToken)) {
            // 从 JWT 中获取用户信息
            Integer userId = jwtUtil.getUserIdFromJwt(authToken);
            String role = jwtUtil.getRoleFromJwt(authToken);

            // 创建一个认证令牌（Authentication Token），并将其设置到 Spring Security 的上下文中
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userId, null, Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role)));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // 将请求和响应传递给过滤器链的下一个元素
        chain.doFilter(request, response);
    }

    private String getAuthToken(HttpServletRequest request) {
        // 从 HTTP 请求头中获取 "Authorization" 字段
        String authToken = request.getHeader(AUTH_TOKEN_KEY);

        // 如果 "Authorization" 字段为空，从 Cookie 中获取 JWT
        if (authToken == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (AUTH_TOKEN_KEY.equals(cookie.getName())) {
                        authToken = cookie.getValue();
                        break;
                    }
                }
            }
        }

        // 如果 "Authorization" 字段和 Cookie 中都没有找到 JWT，从请求参数中获取 JWT
        if (authToken == null) {
            authToken = request.getParameter(AUTH_TOKEN_KEY);
        }

        return authToken;
    }
}