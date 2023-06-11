package com.wuan.attendance.service.impl;

import com.wuan.attendance.dto.WeeklyReportDTO;
import com.wuan.attendance.exception.UserException;
import com.wuan.attendance.exception.WeeklyReportException;
import com.wuan.attendance.mapper.UserMapper;
import com.wuan.attendance.mapper.WeeklyReportMapper;
import com.wuan.attendance.model.WeeklyReport;
import com.wuan.attendance.service.WeeklyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeeklyReportServiceImpl implements WeeklyReportService {
    private final WeeklyReportMapper weeklyReportMapper;
    private final UserMapper userMapper;

    @Autowired
    public WeeklyReportServiceImpl(WeeklyReportMapper weeklyReportMapper, UserMapper userMapper) {
        this.weeklyReportMapper = weeklyReportMapper;
        this.userMapper = userMapper;
    }

    @Override
    public List<WeeklyReportDTO> findAll() {
        return weeklyReportMapper.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public WeeklyReportDTO findById(Integer id) {
        return convertToDTO(weeklyReportMapper.findById(id));
    }

    @Override
    public List<WeeklyReportDTO> findByUserId(Integer userId) {
        if (userId == null) {
            throw new UserException("找不到该用户：userId为空！");
        }
        return weeklyReportMapper.findByUserId(userId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<WeeklyReportDTO> findByUserEmail(String email) {
        Integer userId = userMapper.findByEmail(email).getId();
        return findByUserId(userId);
    }

    public WeeklyReportDTO findByUserEmailAndWeekNumber(String email, Integer weekNumber) {
        List<WeeklyReportDTO> weeklyReportDTOs = findByUserEmail(email);
        for (WeeklyReportDTO weeklyReportDTO : weeklyReportDTOs) {
            if (weeklyReportDTO.getWeekNumber().equals(weekNumber)) {
                return weeklyReportDTO;
            }
        }
        return null;
    }

    @Override
    public boolean create(WeeklyReportDTO weeklyReportDTO) {
        return weeklyReportMapper.create(convertToModel(weeklyReportDTO)) > 0;
    }

    @Override
    public boolean update(WeeklyReportDTO weeklyReportDTO) {
        return weeklyReportMapper.update(convertToModel(weeklyReportDTO)) > 0;
    }

    @Override
    public boolean deleteById(Integer id) {
        return weeklyReportMapper.deleteById(id) > 0;
    }

    @Override
    public boolean deleteByUserIdAndWeekNumber(Integer userId, Integer weekNumber) {
        return weeklyReportMapper.deleteByUserIdAndWeekNumber(userId, weekNumber) > 0;
    }

    private WeeklyReportDTO convertToDTO(WeeklyReport weeklyReport) {
        if (weeklyReport == null) {
            return null;
        }
        WeeklyReportDTO weeklyReportDTO = new WeeklyReportDTO();
        weeklyReportDTO.setId(weeklyReport.getId());
        weeklyReportDTO.setUserId(weeklyReport.getUserId());
        weeklyReportDTO.setGroupId(weeklyReport.getGroupId());
        weeklyReportDTO.setWeekNumber(weeklyReport.getWeekNumber());
        weeklyReportDTO.setContentCompleted(weeklyReport.getContentCompleted());
        weeklyReportDTO.setContentProblems(weeklyReport.getContentProblems());
        weeklyReportDTO.setContentPlan(weeklyReport.getContentPlan());
        weeklyReportDTO.setContentLink(weeklyReport.getContentLink());
        weeklyReportDTO.setStatus(weeklyReport.getStatus());
        return weeklyReportDTO;
    }

    private WeeklyReport convertToModel(WeeklyReportDTO weeklyReportDTO) {
        if (weeklyReportDTO == null) {
            return null;
        }
        WeeklyReport weeklyReport = new WeeklyReport();
        weeklyReport.setId(weeklyReportDTO.getId());
        weeklyReport.setUserId(weeklyReportDTO.getUserId());
        weeklyReport.setGroupId(weeklyReportDTO.getGroupId());
        weeklyReport.setWeekNumber(weeklyReportDTO.getWeekNumber());
        weeklyReport.setContentCompleted(weeklyReportDTO.getContentCompleted());
        weeklyReport.setContentProblems(weeklyReportDTO.getContentProblems());
        weeklyReport.setContentPlan(weeklyReportDTO.getContentPlan());
        weeklyReport.setContentLink(weeklyReportDTO.getContentLink());
        weeklyReport.setStatus(weeklyReportDTO.getStatus());
        return weeklyReport;
    }
}
