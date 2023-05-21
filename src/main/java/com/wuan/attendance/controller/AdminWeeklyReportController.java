package com.wuan.attendance.controller;

import com.wuan.attendance.dto.WeeklyReportDTO;
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

    @Autowired
    private WeeklyReportService weeklyReportService;

    @GetMapping
    public ResponseEntity<List<WeeklyReportDTO>> getAllWeeklyReports() {
        List<WeeklyReportDTO> weeklyReports = weeklyReportService.findAll();
        return ResponseEntity.ok(weeklyReports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getWeeklyReport(@PathVariable Integer id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("未指定要获取的周报的ID");
        }

        if (weeklyReportService.findById(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Weekly report does not exist");
        }

        WeeklyReportDTO weeklyReport = weeklyReportService.findById(id);
        return ResponseEntity.ok(weeklyReport);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateWeeklyReport(@PathVariable Integer id, @RequestBody WeeklyReportDTO weeklyReportDTO) {
        // 在这个方法中，我们假设管理员可以直接通过id更新周报，而无需知道userId

        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("未指定要更新的周报的ID");
        }

        // 检查周报是否存在
        if (weeklyReportService.findById(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Weekly report does not exist");
        }

        boolean updated = weeklyReportService.update(weeklyReportDTO);
        if (updated) {
            return ResponseEntity.ok("Update successful");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update weekly report failed");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWeeklyReport(@PathVariable Integer id) {
        // 在这个方法中，我们假设管理员可以直接通过id删除周报，而无需知道userId

        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("未指定要删除的周报的ID");
        }

        if (weeklyReportService.findById(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Weekly report does not exist");
        }

        boolean deleted = weeklyReportService.deleteById(id);
        if (deleted) {
            return ResponseEntity.ok("Delete weekly report successful");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete weekly report failed");
        }
    }
}
