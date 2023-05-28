package com.wuan.attendance.controller;

import com.wuan.attendance.dto.UserDTO;
import com.wuan.attendance.exception.UserException;
import com.wuan.attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Integer userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("未指定要查询的用户ID");
        }

        UserDTO user = userService.findById(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody UserDTO userDTO) {
        if (userDTO.getId() != null) {
            throw new UserException("User already exists!");
        }

        boolean created = userService.insert(userDTO);
        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Create user failed");
        }
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<Object> updateUserById(@PathVariable Integer userId, @RequestBody UserDTO userDTO) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("未指定要更新的用户的ID");
        }

        UserDTO existingUser = userService.findById(userId);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
        } else {
            boolean updated = userService.update(userDTO);
            if (updated) {
                return ResponseEntity.ok(userDTO);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update user failed");
            }
        }
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("未指定要删除的用户的ID");
        }

        if (userService.findById(userId) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
        }

        boolean deleted = userService.delete(userId);
        if (deleted) {
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete user failed");
        }
    }

    @PutMapping("/change-password/{userId}")
    public ResponseEntity<String> changePassword(@PathVariable Integer userId, @RequestBody Map<String, String> passwordData) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("未指定要修改的用户的ID");
        }

        UserDTO user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
        }

        String newPassword = passwordData.get("newPassword");
        boolean changed = userService.changePassword(user, newPassword);
        if (changed) {
            return ResponseEntity.ok("Change password successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Change password failed");
        }
    }
}
