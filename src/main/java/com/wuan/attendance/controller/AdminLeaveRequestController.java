package com.wuan.attendance.controller;

import com.wuan.attendance.dto.LeaveRequestDTO;
import com.wuan.attendance.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/leave-requests")
@PreAuthorize("hasRole('ADMIN')")
public class AdminLeaveRequestController {
    @Autowired
    private LeaveRequestService leaveRequestService;

    @GetMapping
    public ResponseEntity<Iterable<LeaveRequestDTO>> getAllLeaveRequests() {
        return ResponseEntity.ok(leaveRequestService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getLeaveRequestById(@PathVariable Integer id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id is not provided");
        }

        LeaveRequestDTO leaveRequest = leaveRequestService.findById(id);
        if (leaveRequest == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Leave request does not exist");
        } else {
            return ResponseEntity.ok(leaveRequest);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createLeaveRequest(@RequestBody LeaveRequestDTO leaveRequestDTO) {
        if (leaveRequestDTO.getId() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Leave request already exists");
        }

        boolean created = leaveRequestService.insert(leaveRequestDTO);
        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED).body(leaveRequestDTO);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Create leave request failed");
        }
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<Object> updateLeaveRequest(@PathVariable Integer userId, @RequestBody LeaveRequestDTO leaveRequestDTO) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User id is not provided");
        }

        boolean updated = leaveRequestService.update(leaveRequestDTO);
        if (updated) {
            return ResponseEntity.ok(leaveRequestDTO);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update leave request failed");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteLeaveRequest(@PathVariable Integer id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id is not provided");
        }

        if (leaveRequestService.findById(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Leave request does not exist");
        }

        boolean deleted = leaveRequestService.delete(id);
        if (deleted) {
            return ResponseEntity.ok("Delete leave request successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete leave request failed");
        }
    }
}
