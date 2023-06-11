package com.wuan.attendance.controller;

import com.wuan.attendance.dto.WeeklyReportDTO;
import com.wuan.attendance.dto.WeeklyReportUpdateRequest;
import com.wuan.attendance.service.WeeklyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/weekly-reports")
@PreAuthorize("hasRole('ADMIN')") // 需要安全框架的支持，例如Spring Security
public class AdminWeeklyReportController {

    private final WeeklyReportService weeklyReportService;

    @Autowired
    public AdminWeeklyReportController(WeeklyReportService weeklyReportService) {
        this.weeklyReportService = weeklyReportService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<WeeklyReportDTO>> getAllWeeklyReports() {
        List<WeeklyReportDTO> weeklyReports = weeklyReportService.findAll();
        return ResponseEntity.ok(weeklyReports);
    }

    @GetMapping("/{email}")
    public ResponseEntity<Object> getWeeklyReport(@PathVariable String email) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is missing");
        }

        List<WeeklyReportDTO> weeklyReports = weeklyReportService.findByUserEmail(email);
        if (weeklyReports.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No weekly report found for the provided email");
        }

        return ResponseEntity.ok(weeklyReports);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createWeeklyReportForUser(@RequestBody WeeklyReportUpdateRequest createRequest) {
        String email = createRequest.getEmail();
        Integer weekNumber = createRequest.getWeekNumber();
        WeeklyReportDTO weeklyReportDTO = createRequest.getWeeklyReport();

        if (email == null || email.isEmpty() || weekNumber == null || weeklyReportDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email, week number, or weekly report data is missing");
        }

        // Check if the weekly report already exists
        WeeklyReportDTO existingWeeklyReport = weeklyReportService.findByUserEmailAndWeekNumber(email, weekNumber);
        if (existingWeeklyReport != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Weekly report already exists");
        }

        boolean created = weeklyReportService.create(weeklyReportDTO);
        if (created) {
            return ResponseEntity.ok("Create weekly report successful");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Create weekly report failed");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateWeeklyReportForUser(@RequestBody WeeklyReportUpdateRequest updateRequest) {
        String email = updateRequest.getEmail();
        Integer weekNumber = updateRequest.getWeekNumber();
        WeeklyReportDTO weeklyReportDTO = updateRequest.getWeeklyReport();

        if (email == null || email.isEmpty() || weekNumber == null || weeklyReportDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email, week number, or weekly report data is missing");
        }

        // 检查周报是否存在
        WeeklyReportDTO existingWeeklyReport = weeklyReportService.findByUserEmailAndWeekNumber(email, weekNumber);
        if (existingWeeklyReport == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Weekly report does not exist");
        }

        boolean updated = weeklyReportService.update(weeklyReportDTO);
        if (updated) {
            return ResponseEntity.ok("Update successful");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update weekly report failed");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteOneWeeklyReportForUser(@RequestBody WeeklyReportUpdateRequest deleteRequest) {
        String email = deleteRequest.getEmail();
        Integer weekNumber = deleteRequest.getWeekNumber();

        if (email == null || email.isEmpty() || weekNumber == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or week number is missing");
        }

        // 检查周报是否存在
        WeeklyReportDTO existingWeeklyReport = weeklyReportService.findByUserEmailAndWeekNumber(email, weekNumber);
        if (existingWeeklyReport == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Weekly report does not exist");
        }

        boolean deleted = weeklyReportService.deleteById(existingWeeklyReport.getId());
        if (deleted) {
            return ResponseEntity.ok("Delete weekly report successful");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete weekly report failed");
        }
    }

//    @ExceptionHandler(WeeklyReportException.class)
//    public ResponseEntity<String> handleWeeklyReportException(WeeklyReportException e) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//    }
}
