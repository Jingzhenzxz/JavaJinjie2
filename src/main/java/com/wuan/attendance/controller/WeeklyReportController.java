package com.wuan.attendance.controller;

import com.wuan.attendance.dto.WeeklyReportDTO;
import com.wuan.attendance.service.WeeklyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/user/weekly-reports")
public class WeeklyReportController {

    private final WeeklyReportService weeklyReportService;

    @Autowired
    public WeeklyReportController(WeeklyReportService weeklyReportService) {
        this.weeklyReportService = weeklyReportService;
    }

    @GetMapping
    public ResponseEntity<Object> getMyWeeklyReports(HttpServletRequest request, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        Integer userId = Integer.valueOf(principal.getName());
        List<WeeklyReportDTO> weeklyReports = weeklyReportService.findByUserId(userId);
        return ResponseEntity.ok(weeklyReports);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createMyWeeklyReport(@RequestBody WeeklyReportDTO weeklyReportDTO, HttpServletRequest request, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        boolean created = weeklyReportService.create(weeklyReportDTO);
        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED).body(weeklyReportDTO);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Create weekly report failed");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateMyWeeklyReport(@RequestBody WeeklyReportDTO weeklyReportDTO, HttpServletRequest request, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        Integer userId = Integer.valueOf(principal.getName());
        // 检查周报是否存在
        WeeklyReportDTO existingWeeklyReport = weeklyReportService.findById(userId);
        if (existingWeeklyReport == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Weekly report does not exist");
        }

        boolean updated = weeklyReportService.update(weeklyReportDTO);
        if (updated) {
            return ResponseEntity.ok("Update successful");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMyWeeklyReport(HttpServletRequest request, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        Integer userId = Integer.valueOf(principal.getName());
        // 从请求中获取要删除的周报的周数
        Integer weekNumber = (Integer) request.getAttribute("weekNumber");
        // 删除周报
        boolean deletedWeeklyReportSuccessful = weeklyReportService.deleteByUserIdAndWeekNumber(userId, weekNumber);
        if (deletedWeeklyReportSuccessful) {
            return ResponseEntity.ok("Delete weekly report successful");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete weekly report failed");
        }
    }
}
