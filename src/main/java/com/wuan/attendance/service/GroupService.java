package com.wuan.attendance.service;

import com.wuan.attendance.dto.GroupDTO;

import java.util.List;

public interface GroupService {
    List<GroupDTO> findAll();

    GroupDTO findById(Integer id);

    GroupDTO findByName(String name);

    boolean insert(GroupDTO groupDTO);

    boolean update(GroupDTO groupDTO);

    boolean delete(Integer id);
}
