package com.wuan.attendance.service;

import com.wuan.attendance.dto.RegisterRequest;
import com.wuan.attendance.dto.UserDTO;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<?> register(RegisterRequest registerRequest);

    ResponseEntity<?> login(String email, String password);
}
