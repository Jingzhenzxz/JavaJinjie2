package com.wuan.attendance.service.impl;

import com.wuan.attendance.dto.RegisterRequest;
import com.wuan.attendance.dto.UserDTO;
import com.wuan.attendance.exception.UsernameAlreadyExistsException;
import com.wuan.attendance.mapper.UserMapper;
import com.wuan.attendance.model.User;
import com.wuan.attendance.service.AuthenticationService;
import com.wuan.attendance.service.UserService;
import com.wuan.attendance.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDTO login(String email, String password) {
        User user = userMapper.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            // Convert the User model to UserDTO
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            return userDTO;
        }
        return null;
    }

    @Autowired
    private UserService userService;

    @Override
    public UserDTO register(RegisterRequest registerRequest) {
        // 检查用户名是否已经存在
        if (userService.findByUsername(registerRequest.getUsername()) != null) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        // 创建新用户
        UserDTO newUser = new UserDTO();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(PasswordUtil.encode(registerRequest.getPassword()));
        newUser.setRole("USER"); // 或其他默认角色

        // 将新用户插入数据库
        userService.insert(newUser);

        // 返回新创建的用户
        return newUser;
    }

}