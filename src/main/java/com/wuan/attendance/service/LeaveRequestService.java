package com.wuan.attendance.service;

import com.wuan.attendance.dto.LeaveRequestDTO;

import java.util.List;

public interface LeaveRequestService {
    List<LeaveRequestDTO> findAll();

    LeaveRequestDTO findById(Integer id);

    LeaveRequestDTO findByUserIdAndWeekNumber(Integer userId, Integer weekNumber);

    boolean insert(LeaveRequestDTO leaveRequestDTO);

    boolean update(LeaveRequestDTO leaveRequestDTO);

    boolean delete(Integer id);
}
