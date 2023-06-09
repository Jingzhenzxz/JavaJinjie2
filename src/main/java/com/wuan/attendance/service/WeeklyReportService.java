package com.wuan.attendance.service;

import com.wuan.attendance.dto.WeeklyReportDTO;

import java.util.List;

public interface WeeklyReportService {
    List<WeeklyReportDTO> findAll();

    WeeklyReportDTO findById(Integer id);

    List<WeeklyReportDTO> findByUserId(Integer userId);

    List<WeeklyReportDTO> findByUserEmail(String email);

    WeeklyReportDTO findByUserEmailAndWeekNumber(String email, Integer weekNumber);

    boolean create(WeeklyReportDTO weeklyReportDTO);

    boolean update(WeeklyReportDTO weeklyReportDTO);

    boolean deleteById(Integer id);

    boolean deleteByUserIdAndWeekNumber(Integer userId, Integer weekNumber);
}
