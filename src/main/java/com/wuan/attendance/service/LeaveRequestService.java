package com.wuan.attendance.service;

import com.wuan.attendance.dto.LeaveRequestDTO;

import java.util.List;

public interface LeaveRequestService {
    List<LeaveRequestDTO> findAll();

    LeaveRequestDTO findById(Integer id);

    List<LeaveRequestDTO> findByUserId(Integer userId);

    boolean insert(LeaveRequestDTO leaveRequestDTO);

    boolean update(LeaveRequestDTO leaveRequestDTO);

    boolean delete(Integer id);
    // Other custom methods if needed
}
