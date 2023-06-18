package com.wuan.attendance.controller;

import com.wuan.attendance.dto.UserDTO;
import com.wuan.attendance.exception.UserException;
import com.wuan.attendance.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {
    private final UserService userService;

    @Autowired
    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{email}")
    public ResponseEntity<Object> getUserByEmail(@PathVariable String email) {
        if (email == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("未指定要查询的用户ID");
        }

        UserDTO user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody UserDTO userDTO) {
        if (userDTO.getEmail() == null || userDTO.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or password is missing");
        }

        UserDTO existingUser = userService.findByEmail(userDTO.getEmail());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }

        boolean created = userService.insert(userDTO);
        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Create user failed");
        }
    }

    @PatchMapping("/update/{email}")
    public ResponseEntity<Object> updateUserById(@PathVariable String email, @RequestBody UserDTO userDTO) {
        if (email == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is missing");
        }

        UserDTO existingUser = userService.findByEmail(email);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
        } else {
            userDTO.setId(existingUser.getId()); // Ensure the correct user ID
            boolean updated = userService.update(userDTO);
            if (updated) {
                return ResponseEntity.ok(userDTO);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update user failed");
            }
        }
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email) {
        if (email == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is missing");
        }

        UserDTO user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
        }

        boolean deleted = userService.deleteById(user.getId());
        if (deleted) {
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete user failed");
        }
    }

    @PutMapping("/change-password/{email}")
    public ResponseEntity<String> changePassword(@PathVariable String email, @RequestBody Map<String, String> passwordData) {
        if (email == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is missing");
        }

        UserDTO user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
        }

        String newPassword = passwordData.get("newPassword");
        if (newPassword == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password is missing");
        }

        boolean changed = userService.changePassword(user, newPassword);
        if (changed) {
            return ResponseEntity.ok("Change password successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Change password failed");
        }
    }
}
