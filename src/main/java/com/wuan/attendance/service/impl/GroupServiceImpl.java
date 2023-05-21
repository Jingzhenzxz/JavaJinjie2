package com.wuan.attendance.service.impl;

import com.wuan.attendance.dto.GroupDTO;
import com.wuan.attendance.exception.GroupNotFoundException;
import com.wuan.attendance.mapper.GroupMapper;
import com.wuan.attendance.mapper.UserMapper;
import com.wuan.attendance.model.Group;
import com.wuan.attendance.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<GroupDTO> findAll() {
        return groupMapper.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public GroupDTO findById(Integer id) {
        Group group = groupMapper.findById(id);
        if (group == null) {
            throw new GroupNotFoundException("Group not found with id: " + id);
        }
        return convertToDTO(group);
    }

    @Override
    public boolean insert(GroupDTO groupDTO) {
        return groupMapper.insert(convertToModel(groupDTO)) > 0;
    }

    @Override
    public boolean update(GroupDTO groupDTO) {
        return groupMapper.update(convertToModel(groupDTO)) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        return groupMapper.delete(id) > 0;
    }

    private GroupDTO convertToDTO(Group group) {
        if (group == null) {
            return null;
        }
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(group.getId());
        groupDTO.setName(group.getName());
        return groupDTO;
    }

    private Group convertToModel(GroupDTO groupDTO) {
        if (groupDTO == null) {
            return null;
        }
        Group group = new Group();
        group.setId(groupDTO.getId());
        group.setName(groupDTO.getName());
        return group;
    }
}
