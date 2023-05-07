package com.wuan.attendance.service;

import com.wuan.attendance.dto.UserDTO;

public interface AuthenticationService {
    UserDTO login(String username, String password);
    UserDTO register(String username, String password);
}
