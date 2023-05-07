package com.wuan.attendance.service;

import com.wuan.attendance.dto.UserDTO;
import com.wuan.attendance.model.User;

import java.util.List;

public interface UserService {
    List<UserDTO> findAll();
    UserDTO findById(Integer id);
    UserDTO findByEmail(String email);
    UserDTO findByUsername(String username);
    boolean insert(UserDTO userDTO);
    boolean update(UserDTO userDTO);
    boolean delete(Integer id);
    boolean changePassword(User user, String newPassword);
}
