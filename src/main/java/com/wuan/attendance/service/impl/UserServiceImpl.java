package com.wuan.attendance.service.impl;

import com.wuan.attendance.dto.UserDTO;
import com.wuan.attendance.exception.UserNotFoundException;
import com.wuan.attendance.mapper.UserMapper;
import com.wuan.attendance.model.User;
import com.wuan.attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<UserDTO> findAll() {
        return userMapper.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO findById(Integer id) {
        User user = userMapper.findById(id);
        if (user == null) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        return convertToDTO(user);
    }

    @Override
    public UserDTO findByEmail(String email) {
        User user = userMapper.findByEmail(email);
        if (user == null) {
            return null;
        }
        return convertToDTO(user);
    }

    @Override
    public UserDTO findByUsername(String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("User not found with username: " + username);
        }
        return convertToDTO(user);
    }

    @Override
    public boolean insert(UserDTO userDTO) {
        return userMapper.insert(convertToModel(userDTO)) > 0;
    }

    @Override
    public boolean update(UserDTO userDTO) {
        return userMapper.update(convertToModel(userDTO)) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        return userMapper.delete(id) > 0;
    }

    @Override
    public boolean changePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        int rowsAffected = userMapper.update(user);
        return rowsAffected > 0;
    }

    private UserDTO convertToDTO(User user) {
        // Convert the User object to UserDto object
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setQQ(user.getQQ());
        userDTO.setPassword(user.getPassword());
        userDTO.setGroupId(user.getGroupId());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        return userDTO;
    }

    private User convertToModel(UserDTO userDTO) {
        // Convert the UserDto object to User object
        if (userDTO == null) {
            return null;
        }
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setQQ(userDTO.getQQ());
        user.setPassword(userDTO.getPassword());
        user.setGroupId(userDTO.getGroupId());
        user.setCreatedAt(userDTO.getCreatedAt());
        user.setUpdatedAt(userDTO.getUpdatedAt());
        return user;
    }
}
