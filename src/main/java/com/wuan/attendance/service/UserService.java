package com.wuan.attendance.service;

import com.wuan.attendance.dto.UserDTO;
import com.wuan.attendance.model.User;

import java.util.List;

public interface UserService {
    List<UserDTO> findAll();
    UserDTO findById(Integer id);
    UserDTO findByEmail(String email);
    UserDTO findByUsername(String username);
    boolean insert(User user); // 我们将User对象作为参数使用，因为我们假设客户端发送的数据已经是合适的格式，并且不需要额外的处理。
    boolean update(User user);
    boolean delete(Integer id);
    boolean changePassword(User user, String newPassword);
    // Other custom methods if needed
}
