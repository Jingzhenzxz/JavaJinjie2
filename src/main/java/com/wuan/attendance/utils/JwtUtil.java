package com.wuan.attendance.utils;

import com.wuan.attendance.dto.UserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Service
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private SecretKey getSecretKey() {
        // 从配置的密钥字符串生成一个SecretKey对象，用于签名和验证JWT。
        // 这个SecretKey对象是对称的，即同一个密钥既用于签名JWT也用于验证JWT。
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(UserDTO userDTO) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(userDTO.getId().toString())
                .claim("email", userDTO.getEmail())
                .claim("role", userDTO.getUserRole().toString())  // 添加一个名为 "role" 的声明，值是用户的角色
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }


    public String getEmailFromJwt(String authHeader) {
        // 把 Authentication 字段的值中的“Bearer ”去掉。
        String token = authHeader.replace("Bearer ", "");
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                // 返回一个Claims对象。然后，它从这个Claims对象中获取并返回主题，即电子邮件地址。
                .getBody();

        return claims.get("email", String.class);
    }

    public String getRoleFromJwt(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }

    public Integer getUserIdFromJwt(String authHeader) {
        // 从给定的JWT中提取用户ID
        // 使用Jwts.parserBuilder()来创建一个新的JWT解析器

        String token = authHeader.replace("Bearer ", "");
        Claims claims = Jwts.parserBuilder()
                // 使用setSigningKey方法设置解析器的签名密钥
                .setSigningKey(getSecretKey())
                // 使用parseClaimsJws方法解析JWT
                .build()
                .parseClaimsJws(token)
                // 获取 JWT 的全部有效负载
                .getBody();

        return Integer.parseInt(claims.getSubject());
        // 将主体部分（也就是用户ID）转换为整数，并返回。
        // getSubject() 是用来获取 JWT 有效负载中的一个特定声明，即主体部分。
    }

    // 验证一个JWT。它试图解析这个JWT，如果可以成功解析（即JWT格式正确、签名正确且未过期），
    // 那么返回true。如果在解析过程中出现任何异常，那么返回false。
    public boolean validateToken(String authHeader) {
        try {
            // 把 Authentication 字段的值中的“Bearer ”去掉。
            String authToken = authHeader.replace("Bearer ", "");
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
