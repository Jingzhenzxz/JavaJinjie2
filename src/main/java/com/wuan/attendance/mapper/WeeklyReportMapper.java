package com.wuan.attendance.mapper;

import com.wuan.attendance.model.WeeklyReport;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WeeklyReportMapper {
    List<WeeklyReport> findAll();

    WeeklyReport findById(Integer id);

    List<WeeklyReport> findByUserId(Integer userId);

    int create(WeeklyReport weeklyReport);

    int update(WeeklyReport weeklyReport);

    int deleteById(Integer id);

    int deleteByUserIdAndWeekNumber(Integer userId, Integer weekNumber);
}
