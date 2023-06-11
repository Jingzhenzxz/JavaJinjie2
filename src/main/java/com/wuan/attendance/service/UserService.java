package com.wuan.attendance.service;

import com.wuan.attendance.dto.GroupDTO;
import com.wuan.attendance.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> findAll();

    UserDTO findById(Integer id);

    UserDTO findByEmail(String email);

    UserDTO findByUsername(String username);

    boolean insert(UserDTO userDTO);

    boolean update(UserDTO userDTO);

    boolean deleteById(Integer userId);

    boolean changePassword(UserDTO userDTO, String newPassword);
}
