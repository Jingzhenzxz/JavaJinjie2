package com.wuan.attendance.controller;

import com.wuan.attendance.dto.LeaveRequestDTO;
import com.wuan.attendance.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user/leave-requests")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;
    private HttpServletRequest httpServletRequest;

    @GetMapping
    public ResponseEntity<Object> getMyLeaveRequest() {
        Integer userId = (Integer) httpServletRequest.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        LeaveRequestDTO leaveRequest = leaveRequestService.findById(userId);
        return ResponseEntity.ok(leaveRequest);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateMyLeaveRequest(@RequestBody LeaveRequestDTO leaveRequestDTO) {
        boolean updated = leaveRequestService.update(leaveRequestDTO);
        if (updated) {
            return ResponseEntity.ok(leaveRequestDTO);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Update leave request failed");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMyLeaveRequest() {
        Integer userId = (Integer) httpServletRequest.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        boolean deleted = leaveRequestService.delete(userId);
        if (deleted) {
            return ResponseEntity.ok("Delete leave request successfully");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Delete leave request failed");
        }
    }


}
