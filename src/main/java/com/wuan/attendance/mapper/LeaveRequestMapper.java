package com.wuan.attendance.mapper;

import com.wuan.attendance.model.LeaveRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LeaveRequestMapper {
    List<LeaveRequest> findAll();
    LeaveRequest findById(Integer id);
    int insert(LeaveRequest leaveRequest);
    int update(LeaveRequest leaveRequest);
    int delete(Integer id);
    // Other custom methods if needed
}
