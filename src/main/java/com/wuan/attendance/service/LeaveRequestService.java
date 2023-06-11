package com.wuan.attendance.service;

import com.wuan.attendance.dto.LeaveRequestDTO;

import java.util.List;

public interface LeaveRequestService {
    List<LeaveRequestDTO> findAll();

    LeaveRequestDTO findById(Integer id);

    List<LeaveRequestDTO> findByUserId(Integer userId);

    LeaveRequestDTO findByUserIdAndWeekNumber(Integer userId, Integer weekNumber);

    LeaveRequestDTO findByUserEmailAndWeekNumber(String email, Integer weekNumber);

    boolean create(LeaveRequestDTO leaveRequestDTO);

    boolean update(LeaveRequestDTO leaveRequestDTO);

    boolean deleteByUserIdAndWeekNumber(Integer id, Integer weekNumber);

    boolean deleteByEmailAndWeekNumber(String email, Integer weekNumber);
}
