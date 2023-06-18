package com.wuan.attendance.controller;

import com.wuan.attendance.dto.UserDTO;
import com.wuan.attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
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
    public ResponseEntity<Object> getMyAccountById(Principal principal) {
        // 从 Principal 中获取用户 ID
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }
        Integer userId = Integer.valueOf(principal.getName());

        UserDTO me = userService.findById(userId);
        return ResponseEntity.ok(me);
    }

    @PatchMapping("/update")
    public ResponseEntity<Object> updateMyAccount(@RequestBody UserDTO userDTO, Principal principal) {
        if (principal == null) {
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
    public ResponseEntity<String> deleteMyAccount(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }
        Integer userId = Integer.valueOf(principal.getName());

        boolean userIsDeleted = userService.deleteById(userId);

        if (userIsDeleted) {
            return ResponseEntity.ok("Delete user successful");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete user failed");
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changeMyPassword(@RequestBody Map<String, String> passwordData, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }
        Integer userId = Integer.valueOf(principal.getName());

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
