package com.wuan.attendance.service.impl;

import com.wuan.attendance.dto.RegisterRequest;
import com.wuan.attendance.dto.UserDTO;
import com.wuan.attendance.enums.UserRole;
import com.wuan.attendance.exception.AuthenticationException;
import com.wuan.attendance.exception.UserException;
import com.wuan.attendance.service.AuthenticationService;
import com.wuan.attendance.service.UserService;
import com.wuan.attendance.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private UserService userService;

    @Override
    public UserDTO login(String email, String password) {
        UserDTO user = userService.findByEmail(email);

        if (user == null) {
            throw new UserException("User not found (email: " + email + ")");
        }

        boolean passwordMatch = PasswordUtil.verifyPassword(password, user.getPassword());
        if (!passwordMatch) {
            throw new AuthenticationException("Invalid password");
        }

        return user;
    }

    @Override
    public UserDTO register(RegisterRequest registerRequest) {
        // 检查密码和确认密码是否相同
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and confirm password do not match");
        }

        // 检查邮箱是否已经存在
        if (userService.findByEmail(registerRequest.getEmail()) != null) {
            throw new UserException("UserEmail already exists!");
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
        return newUser;
    }
}