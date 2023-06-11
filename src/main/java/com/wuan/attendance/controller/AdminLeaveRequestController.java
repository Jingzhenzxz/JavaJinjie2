package com.wuan.attendance.controller;

import com.wuan.attendance.dto.LeaveRequestDTO;
import com.wuan.attendance.dto.LeaveRequestUpdateRequest;
import com.wuan.attendance.dto.WeeklyReportDTO;
import com.wuan.attendance.service.LeaveRequestService;
import com.wuan.attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/leave-requests")
@PreAuthorize("hasRole('ADMIN')")
public class AdminLeaveRequestController {
    private final LeaveRequestService leaveRequestService;
    private final UserService userService;

    @Autowired
    public AdminLeaveRequestController(LeaveRequestService leaveRequestService, UserService userService) {
        this.leaveRequestService = leaveRequestService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<LeaveRequestDTO>> getAllLeaveRequests() {
        return ResponseEntity.ok(leaveRequestService.findAll());
    }

    @GetMapping("/{email}")
    public ResponseEntity<Object> getLeaveRequestByEmail(@PathVariable String email) {
        if (email == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is not provided");
        }

        Integer userId = userService.findByEmail(email).getId();
        List<LeaveRequestDTO> leaveRequests = leaveRequestService.findByUserId(userId);

        if (leaveRequests == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Leave requests do not exist");
        } else {
            return ResponseEntity.ok(leaveRequests);
        }
    }

    @GetMapping("/{email}/{weekNumber}")
    public ResponseEntity<Object> getLeaveRequestByEmailAndWeekNumber(@PathVariable String email, @PathVariable Integer weekNumber) {
        if (email == null || weekNumber == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or week number is not provided");
        }

        Integer userId = userService.findByEmail(email).getId();
        LeaveRequestDTO leaveRequest = leaveRequestService.findByUserIdAndWeekNumber(userId, weekNumber);

        if (leaveRequest == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Leave request does not exist");
        } else {
            return ResponseEntity.ok(leaveRequest);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createLeaveRequestForUser(@RequestBody LeaveRequestUpdateRequest createRequest) {
        String email = createRequest.getEmail();
        Integer weekNumber = createRequest.getWeekNumber();
        LeaveRequestDTO leaveRequestDTO = createRequest.getLeaveRequestDTO();

        if (email == null || email.isEmpty() || weekNumber == null || leaveRequestDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email, week number, or leave request data is missing");
        }

        // Check if the leave request already exists
        LeaveRequestDTO existingLeaveRequest = leaveRequestService.findByUserEmailAndWeekNumber(email, weekNumber);
        if (existingLeaveRequest != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Leave Request already exists");
        }

        boolean created = leaveRequestService.create(leaveRequestDTO);
        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED).body(leaveRequestDTO);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Create leave request failed");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateLeaveRequestForUser(@RequestBody LeaveRequestUpdateRequest updateRequest) {
        String email = updateRequest.getEmail();
        Integer weekNumber = updateRequest.getWeekNumber();
        LeaveRequestDTO leaveRequestDTO = updateRequest.getLeaveRequestDTO();

        if (email == null || email.isEmpty() || weekNumber == null || leaveRequestDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email, week number, or leave request data is missing");
        }

        // 检查请假条是否存在
        LeaveRequestDTO existingLeaveRequest = leaveRequestService.findByUserEmailAndWeekNumber(email, weekNumber);
        if (existingLeaveRequest == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Leave Request does not exist");
        }

        boolean updated = leaveRequestService.update(leaveRequestDTO);
        if (updated) {
            return ResponseEntity.ok(leaveRequestDTO);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update leave request failed");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteLeaveRequestForUser(@RequestBody LeaveRequestUpdateRequest deleteRequest) {
        String email = deleteRequest.getEmail();
        Integer weekNumber = deleteRequest.getWeekNumber();

        if (email == null || email.isEmpty() || weekNumber == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or week number is missing");
        }

        // 检查请假条是否存在
        LeaveRequestDTO existingLeaveRequest = leaveRequestService.findByUserEmailAndWeekNumber(email, weekNumber);
        if (existingLeaveRequest == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Leave Request does not exist");
        }

        boolean deleted = leaveRequestService.deleteByEmailAndWeekNumber(email, weekNumber);
        if (deleted) {
            return ResponseEntity.ok("Delete leave request successful");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete leave request failed");
        }
    }
}
