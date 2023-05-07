package com.wuan.attendance.controller;

import com.wuan.attendance.dto.WeeklyReportDTO;
import com.wuan.attendance.service.WeeklyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/weekly-reports")
public class WeeklyReportController {

    @Autowired
    private WeeklyReportService weeklyReportService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WeeklyReportDTO>> getWeeklyReportsByUserId(@PathVariable Integer userId) {
        List<WeeklyReportDTO> weeklyReports = weeklyReportService.findByUserId(userId);
        return ResponseEntity.ok(weeklyReports);
    }
}
