package com.wuan.attendance.controller;

import com.wuan.attendance.dto.UserDTO;
import com.wuan.attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final HttpServletRequest request;

    @Autowired
    public UserController(UserService userService, HttpServletRequest request) {
        this.userService = userService;
        this.request = request;
    }

    @GetMapping
    public ResponseEntity<Object> getMyAccountById() {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        UserDTO me = userService.findById(userId);
        return ResponseEntity.ok(me);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateMyAccount(@RequestBody UserDTO userDTO) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        boolean updated = userService.update(userDTO);
        if (updated) {
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update user failed");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMyAccount() {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        boolean userIsDeleted = userService.delete(userId);

        if (userIsDeleted) {
            return ResponseEntity.ok("Delete user successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete user failed");
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changeMyPassword(@RequestBody Map<String, String> passwordData) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        UserDTO user = userService.findById(userId);
        String newPassword = passwordData.get("newPassword");
        boolean changed = userService.changePassword(user, newPassword);
        if (changed) {
            return ResponseEntity.ok("Change password successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Change password failed");
        }
    }
}
