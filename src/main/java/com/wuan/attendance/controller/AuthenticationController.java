package com.wuan.attendance.controller;

import com.wuan.attendance.dto.LoginRequest;
import com.wuan.attendance.dto.RegisterRequest;
import com.wuan.attendance.service.AuthenticationService;
import com.wuan.attendance.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, UserService userService) {
        log.debug("注入authenticationService和userService");
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        return authenticationService.register(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        log.debug("能访问登录api");
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        return authenticationService.login(email, password);
    }
}