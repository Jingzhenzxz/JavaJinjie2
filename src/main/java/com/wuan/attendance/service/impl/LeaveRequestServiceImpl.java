package com.wuan.attendance.service.impl;

import com.wuan.attendance.dto.LeaveRequestDTO;
import com.wuan.attendance.exception.UserException;
import com.wuan.attendance.mapper.LeaveRequestMapper;
import com.wuan.attendance.mapper.UserMapper;
import com.wuan.attendance.model.LeaveRequest;
import com.wuan.attendance.model.User;
import com.wuan.attendance.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {
    private final LeaveRequestMapper leaveRequestMapper;
    private final UserMapper userMapper;

    @Autowired
    public LeaveRequestServiceImpl(LeaveRequestMapper leaveRequestMapper, UserMapper userMapper) {
        this.leaveRequestMapper = leaveRequestMapper;
        this.userMapper = userMapper;
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
    public List<LeaveRequestDTO> findByUserId(Integer userId) {
        return leaveRequestMapper.findByUserId(userId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public LeaveRequestDTO findByUserIdAndWeekNumber(Integer userId, Integer weekNumber) {
        return convertToDTO(leaveRequestMapper.findByUserIdAndWeekNumber(userId, weekNumber));
    }

    @Override
    public LeaveRequestDTO findByUserEmailAndWeekNumber(String email, Integer weekNumber) {
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new UserException("没有与该Email对应的用户");
        }

        Integer userId = userMapper.findByEmail(email).getId();
        return findByUserIdAndWeekNumber(userId, weekNumber);
    }

    @Override
    public boolean create(LeaveRequestDTO leaveRequestDTO) {
        return leaveRequestMapper.insert(convertToModel(leaveRequestDTO)) > 0;
    }

    @Override
    public boolean update(LeaveRequestDTO leaveRequestDTO) {
        return leaveRequestMapper.update(convertToModel(leaveRequestDTO)) > 0;
    }

    @Override
    public boolean deleteByUserIdAndWeekNumber(Integer id, Integer weekNumber) {
        return leaveRequestMapper.deleteByUserIdAndWeekNumber(id, weekNumber) > 0;
    }

    @Override
    public boolean deleteByEmailAndWeekNumber(String email, Integer weekNumber) {
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new UserException("没有与该Email对应的用户");
        }

        Integer userId = userMapper.findByEmail(email).getId();
        return deleteByUserIdAndWeekNumber(userId, weekNumber);
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
