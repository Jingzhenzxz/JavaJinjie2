package com.wuan.attendance.controller;

import com.wuan.attendance.dto.UserDTO;
import com.wuan.attendance.service.UserService;
import com.wuan.attendance.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        UserDTO user = userServiceImpl.findById(id);
        return ResponseEntity.ok(user);
    }

    // Other endpoint methods implemented here

}
