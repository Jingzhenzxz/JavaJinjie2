package com.wuan.attendance.service.impl;

import com.wuan.attendance.dto.LoginResponse;
import com.wuan.attendance.dto.RegisterRequest;
import com.wuan.attendance.dto.UserDTO;
import com.wuan.attendance.enums.UserRole;
import com.wuan.attendance.service.AuthenticationService;
import com.wuan.attendance.service.UserService;
import com.wuan.attendance.utils.JwtUtil;
import com.wuan.attendance.utils.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationServiceImpl(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ResponseEntity<?> login(String email, String password) {
        log.debug("调用login方法");
        UserDTO userDTO = userService.findByEmail(email);

        if (userDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found (email: " + email + ")");
        }

        boolean passwordMatch = PasswordUtil.verifyPassword(password, userDTO.getPassword());
        if (!passwordMatch) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        String token = jwtUtil.generateToken(userDTO);
        return ResponseEntity.ok(new LoginResponse(userDTO, token));
    }


    @Override
    public ResponseEntity<?> register(RegisterRequest registerRequest) {
        // 检查密码和确认密码是否相同
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password and confirm password do not match");
        }

        // 检查邮箱是否已经存在
        if (userService.findByEmail(registerRequest.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("UserEmail already exists!");
        }

        // 创建新用户
        UserDTO newUser = new UserDTO();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setQQ(registerRequest.getQQ());
        newUser.setPassword(PasswordUtil.encode(registerRequest.getPassword()));
        newUser.setUserRole(UserRole.USER); // 设置用户级别

        // 将新用户插入数据库
        userService.insert(newUser);
        // 返回新创建的用户
        return ResponseEntity.ok(newUser);
    }
}