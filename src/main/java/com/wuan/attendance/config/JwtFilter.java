package com.wuan.attendance.config;

import com.wuan.attendance.utils.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class JwtFilter extends GenericFilterBean {
    private static final String AUTH_TOKEN_KEY = "Authorization";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // 从请求中获取 JWT
        String authToken = getAuthToken(httpRequest);
        if (authToken != null) {
            // 如果找到 JWT，使用 JwtUtil.getUserIdFromToken 方法从 JWT 中获取用户 ID
            Integer userId = JwtUtil.getUserIdFromToken(authToken);
            // 将用户 ID 设置到请求的属性中
            request.setAttribute("userId", userId);
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