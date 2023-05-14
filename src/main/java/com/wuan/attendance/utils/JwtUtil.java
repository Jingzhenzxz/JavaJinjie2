package com.wuan.attendance.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

// 用来生成和解析JWT（JSON Web Token）
public class JwtUtil {
    private static final String SECRET_KEY = "yourSecretKey";

    public static String generateToken(Integer userId) { // 根据提供的用户ID生成一个JWT
        return Jwts.builder() // 使用Jwts.builder()来创建一个新的JWT构建器
                // 使用setSubject方法设置JWT的主题（这是JWT的核心部分，通常包含主要的验证声明，例如用户ID）
                .setSubject(userId.toString())
                // 使用setIssuedAt方法设置JWT的发行日期
                .setIssuedAt(new Date())
                // 使用signWith方法对JWT进行签名，签名算法是HS256，签名密钥是SECRET_KEY
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                // 使用compact方法生成JWT
                .compact();
    }

    public static Integer getUserIdFromToken(String token) { // 从给定的JWT中提取用户ID
        // 使用Jwts.parser()来创建一个新的JWT解析器
        Claims claims = Jwts.parser()
                // 使用setSigningKey方法设置解析器的签名密钥
                .setSigningKey(SECRET_KEY)
                // 使用parseClaimsJws方法解析JWT
                .parseClaimsJws(token)
                // 获取 JWT 的全部有效负载
                .getBody();
        return Integer.parseInt(claims.getSubject()); // 将主体部分（也就是用户ID）转换为整数，并返回
        // getSubject() 是用来获取 JWT 有效负载中的一个特定声明，即主体部分
    }
}
