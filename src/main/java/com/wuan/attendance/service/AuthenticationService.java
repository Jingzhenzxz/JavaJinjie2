package com.wuan.attendance.service;

import com.wuan.attendance.dto.RegisterRequest;
import com.wuan.attendance.dto.UserDTO;

public interface AuthenticationService {
    UserDTO login(String email, String password);
    UserDTO register(RegisterRequest registerRequest);
}
