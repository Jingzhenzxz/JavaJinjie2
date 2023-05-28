package com.wuan.attendance.service.impl;

import com.wuan.attendance.dto.LeaveRequestDTO;
import com.wuan.attendance.mapper.LeaveRequestMapper;
import com.wuan.attendance.model.LeaveRequest;
import com.wuan.attendance.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {
    private final LeaveRequestMapper leaveRequestMapper;

    @Autowired
    public LeaveRequestServiceImpl(LeaveRequestMapper leaveRequestMapper) {
    this.leaveRequestMapper = leaveRequestMapper;
    }

    @Override
    public List<LeaveRequestDTO> findAll() {
        return leaveRequestMapper.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public LeaveRequestDTO findById(Integer id) {
        return convertToDTO(leaveRequestMapper.findById(id));
    }

    @Override
    public LeaveRequestDTO findByUserIdAndWeekNumber(Integer userId, Integer weekNumber) {
        return convertToDTO(leaveRequestMapper.findByUserIdAndWeekNumber(userId, weekNumber));
    }

    @Override
    public boolean insert(LeaveRequestDTO leaveRequestDTO) {
        return leaveRequestMapper.insert(convertToModel(leaveRequestDTO)) > 0;
    }

    @Override
    public boolean update(LeaveRequestDTO leaveRequestDTO) {
        return leaveRequestMapper.update(convertToModel(leaveRequestDTO)) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        return leaveRequestMapper.delete(id) > 0;
    }

    private LeaveRequestDTO convertToDTO(LeaveRequest leaveRequest) {
        if (leaveRequest == null) {
            return null;
        }
        LeaveRequestDTO leaveRequestDTO = new LeaveRequestDTO();
        leaveRequestDTO.setId(leaveRequest.getId());
        leaveRequestDTO.setUserId(leaveRequest.getUserId());
        leaveRequestDTO.setReason(leaveRequest.getReason());
        leaveRequestDTO.setStatus(leaveRequest.getStatus());
        return leaveRequestDTO;
    }

    private LeaveRequest convertToModel(LeaveRequestDTO leaveRequestDTO) {
        if (leaveRequestDTO == null) {
            return null;
        }
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setId(leaveRequestDTO.getId());
        leaveRequest.setUserId(leaveRequestDTO.getUserId());
        leaveRequest.setReason(leaveRequestDTO.getReason());
        leaveRequest.setStatus(leaveRequestDTO.getStatus());
        return leaveRequest;
    }
}
