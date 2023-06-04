package com.wuan.attendance.controller;

import com.wuan.attendance.dto.LeaveRequestDTO;
import com.wuan.attendance.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/api/user/leave-requests")
public class LeaveRequestController {
    private final LeaveRequestService leaveRequestService;
    private final HttpServletRequest httpServletRequest;

    @Autowired
    public LeaveRequestController(LeaveRequestService leaveRequestService, HttpServletRequest httpServletRequest) {
        this.leaveRequestService = leaveRequestService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getMyLeaveRequest(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        Integer userId = Integer.valueOf(principal.getName());
        LeaveRequestDTO leaveRequest = leaveRequestService.findById(userId);
        return ResponseEntity.ok(leaveRequest);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateMyLeaveRequest(@RequestBody LeaveRequestDTO leaveRequestDTO, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        boolean updated = leaveRequestService.update(leaveRequestDTO);
        if (updated) {
            return ResponseEntity.ok(leaveRequestDTO);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update leave request failed");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMyLeaveRequest(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        Integer userId = Integer.valueOf(principal.getName());
        boolean deleted = leaveRequestService.delete(userId);
        if (deleted) {
            return ResponseEntity.ok("Delete leave request successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete leave request failed");
        }
    }


}
