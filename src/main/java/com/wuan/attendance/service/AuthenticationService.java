package com.wuan.attendance.service;

import com.wuan.attendance.dto.RegisterRequest;
import com.wuan.attendance.dto.UserDTO;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<?> login(String email, String password);

    UserDTO register(RegisterRequest registerRequest);
}
