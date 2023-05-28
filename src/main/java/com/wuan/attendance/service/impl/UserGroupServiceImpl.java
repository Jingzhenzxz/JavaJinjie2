package com.wuan.attendance.service.impl;

import com.wuan.attendance.dto.GroupDTO;
import com.wuan.attendance.dto.UserDTO;
import com.wuan.attendance.mapper.GroupMapper;
import com.wuan.attendance.mapper.UserGroupMapper;
import com.wuan.attendance.mapper.UserMapper;
import com.wuan.attendance.model.Group;
import com.wuan.attendance.model.User;
import com.wuan.attendance.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserGroupServiceImpl implements UserGroupService {
    private final UserGroupMapper userGroupMapper;
    private final GroupMapper groupMapper;
    private final UserMapper userMapper;

    @Autowired
    public UserGroupServiceImpl(UserGroupMapper userGroupMapper, GroupMapper groupMapper, UserMapper userMapper) {
        this.userGroupMapper = userGroupMapper;
        this.groupMapper = groupMapper;
        this.userMapper = userMapper;
    }

    @Override
    public List<GroupDTO> getAllGroupsByUserId(Integer userId) {
        List<Integer> groupIds = userGroupMapper.getAllGroupIdsByUserId(userId);

        List<GroupDTO> groups = new ArrayList<>();
        for (Integer groupId : groupIds) {
            GroupDTO groupDTO = convertGroupModelToGroupDTO(groupMapper.findById(groupId));
            groups.add(groupDTO);
        }
        return groups;
    }

    @Override
    public List<UserDTO> getAllUsersByGroupId(Integer groupId) {
        List<Integer> userIds = userGroupMapper.getAllUserIdsByGroupId(groupId);

        List<UserDTO> users = new ArrayList<>();
        for (Integer userId : userIds) {
            UserDTO userDTO = convertUserModelToUserDTO(userMapper.findById(userId));
            users.add(userDTO);
        }
        return users;
    }

    @Override
    public boolean insertUserGroupRelation(Integer userId, Integer groupId) {
        return userGroupMapper.insertUserGroupRelation(userId, groupId) > 0;
    }

    @Override
    public boolean deleteGroupOfUser(Integer userId, Integer groupId) {
        return userGroupMapper.deleteGroupOfUser(userId, groupId) > 0;
    }

    private UserDTO convertUserModelToUserDTO(User user) {
        // Convert the User object to UserDto object
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setQQ(user.getQQ());
        userDTO.setPassword(user.getPassword());
        userDTO.setGroups(user.getGroups().stream().map(this::convertGroupModelToGroupDTO).collect(Collectors.toList()));
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        return userDTO;
    }

    private User convertUserDTOToUserModel(UserDTO userDTO) {
        // Convert the UserDto object to User object
        if (userDTO == null) {
            return null;
        }
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setQQ(userDTO.getQQ());
        user.setPassword(userDTO.getPassword());
        user.setGroups(userDTO.getGroups().stream().map(this::convertGroupDTOToGroupModel).collect(Collectors.toList()));
        user.setCreatedAt(userDTO.getCreatedAt());
        user.setUpdatedAt(userDTO.getUpdatedAt());
        return user;
    }

    private GroupDTO convertGroupModelToGroupDTO(Group group) {
        // Convert the Group object to GroupDTO object
        if (group == null) {
            return null;
        }
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(group.getId());
        groupDTO.setName(group.getName());
        return groupDTO;
    }

    private Group convertGroupDTOToGroupModel(GroupDTO groupDTO) {
        // Convert the GroupDTO object to Group object
        if (groupDTO == null) {
            return null;
        }
        Group group = new Group();
        group.setId(groupDTO.getId());
        group.setName(groupDTO.getName());
        return group;
    }
}
