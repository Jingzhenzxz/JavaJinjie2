package com.wuan.attendance.service;

import com.wuan.attendance.dto.WeeklyReportDTO;

import java.util.List;

public interface WeeklyReportService {
    List<WeeklyReportDTO> findAll();
    WeeklyReportDTO findById(Integer id);
    List<WeeklyReportDTO> findByUserId(Integer userId);
    boolean insert(WeeklyReportDTO weeklyReportDTO);
    boolean update(WeeklyReportDTO weeklyReportDTO);
    boolean delete(Integer id);
    // Other custom methods if needed
}
