package com.wuan.attendance.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity // 启用 Spring Security
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter) {
        log.debug("注入JwtFilter");
        this.jwtFilter = jwtFilter;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        log.debug("Configuring http security");

        // 禁用 CSRF（跨站请求伪造）保护
        http
                .csrf().disable()
                .httpBasic().disable() // 禁用基本身份验证
                // 定义 URL 的访问权限：路径 /api/authentication/** 的所有请求都被允许（不需要认证），
                // 其他所有请求都需要被认证
                .authorizeRequests()
//                .antMatchers("/api/groups/**").permitAll() // /api/groups/** 的所有请求都被允许（不需要认证）
                .antMatchers("/api/authentication/**").permitAll()
                .anyRequest().authenticated()
                .and()
                // 在 UsernamePasswordAuthenticationFilter 过滤器之前添加 JwtFilter 过滤器
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        log.debug("Added JWT filter before UsernamePasswordAuthenticationFilter");
    }
}
