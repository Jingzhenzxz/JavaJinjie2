package com.wuan.attendance.mapper;

import com.wuan.attendance.model.Group;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GroupMapper {
    List<Group> findAll();

    Group findById(Integer id);
    
    int insert(Group group);

    int update(Group group);

    int delete(Integer id);
}
