package com.wuan.attendance.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // 启用 Spring Security
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 禁用 CSRF（跨站请求伪造）保护
        http.csrf().disable()
                // 定义 URL 的访问权限：路径 /api/authentication/** 和 /api/groups/** 的所有请求都被允许（不需要认证），
                // 其他所有请求都需要被认证
                .authorizeRequests()
                .antMatchers("/api/authentication/**").permitAll()
                .antMatchers("/api/groups/**").permitAll()
                .anyRequest().authenticated()
                .and()
                // 在 UsernamePasswordAuthenticationFilter 过滤器之前添加 JwtFilter 过滤器
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
