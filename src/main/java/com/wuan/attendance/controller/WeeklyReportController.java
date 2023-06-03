package com.wuan.attendance.controller;

import com.wuan.attendance.dto.WeeklyReportDTO;
import com.wuan.attendance.service.WeeklyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<Object> getWeeklyReportsByUserId(HttpServletRequest request) {
        // 从远程调用中获取 userId
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        List<WeeklyReportDTO> weeklyReports = weeklyReportService.findByUserId(userId);
        return ResponseEntity.ok(weeklyReports);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createWeeklyReport(@RequestBody WeeklyReportDTO weeklyReportDTO, HttpServletRequest request) {
        // 从请求中获取 userId
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
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
    public ResponseEntity<String> updateWeeklyReport(@RequestBody WeeklyReportDTO weeklyReportDTO, HttpServletRequest request) {
        // 从请求中获取 userId
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

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
    public ResponseEntity<String> deleteWeeklyReport(HttpServletRequest request) {
        // 从请求中获取 userId
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

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
