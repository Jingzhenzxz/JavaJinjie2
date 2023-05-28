package com.wuan.attendance.service.impl;

import com.wuan.attendance.dto.GroupDTO;
import com.wuan.attendance.dto.UserDTO;
import com.wuan.attendance.exception.UserException;
import com.wuan.attendance.mapper.UserMapper;
import com.wuan.attendance.model.Group;
import com.wuan.attendance.model.User;
import com.wuan.attendance.service.UserGroupService;
import com.wuan.attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserGroupService userGroupService;
    @Autowired
    public UserServiceImpl(UserMapper userMapper, UserGroupService userGroupService) {
        this.userMapper = userMapper;
        this.userGroupService = userGroupService;
    }

    @Override
    public List<UserDTO> findAll() {
        return userMapper.findAll().stream().map(this::convertUserModelToUserDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO findById(Integer id) {
        User user = userMapper.findById(id);
        if (user == null) {
            throw new UserException("User not found with id: " + id);
        }
        return convertUserModelToUserDTO(user);
    }

    @Override
    public UserDTO findByEmail(String email) {
        User user = userMapper.findByEmail(email);
        if (user == null) {
            return null;
        }
        return convertUserModelToUserDTO(user);
    }

    @Override
    public UserDTO findByUsername(String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UserException("User not found with username: " + username);
        }
        return convertUserModelToUserDTO(user);
    }

    @Override
    public boolean insert(UserDTO userDTO) {
        List<GroupDTO> groups = userDTO.getGroups();
        Integer userId = userDTO.getId();
        boolean allUserGroupRelationsAreCreated = true;
        for (GroupDTO group : groups) {
            boolean userGroupRelationIsCreated = userGroupService.insertUserGroupRelation(userId, group.getId());
            allUserGroupRelationsAreCreated = userGroupRelationIsCreated && allUserGroupRelationsAreCreated;
        }
        return userMapper.insert(convertUserDTOToUserModel(userDTO)) > 0 && allUserGroupRelationsAreCreated;
    }

    @Override
    public boolean update(UserDTO userDTO) {
        List<GroupDTO> updatedGroups = userDTO.getGroups();
        Integer userId = userDTO.getId();
        // 该用户的原来的群组
        List<GroupDTO> originalGroups = findById(userId).getGroups();
        // 把它变成HashMap以便查找
        Map<Integer, GroupDTO> updatedGroupMap = updatedGroups.stream()
                .collect(Collectors.toMap(GroupDTO::getId, Function.identity()));

        for (GroupDTO originalGroup : originalGroups) {
            if (!updatedGroupMap.containsKey(originalGroup.getId())) {
                // 如果原来的群组在更新的群组列表中不存在，删除该关系
                userGroupService.deleteGroupOfUser(userId, originalGroup.getId());
            }
        }

        for (GroupDTO updatedGroup : updatedGroups) {
            if (!originalGroups.contains(updatedGroup)) {
                // 如果更新的群组在原来的群组列表中不存在，添加新的关系
                userGroupService.insertUserGroupRelation(userId, updatedGroup.getId());
            }
        }

        return userMapper.update(convertUserDTOToUserModel(userDTO)) > 0;
    }


    @Override
    public boolean delete(Integer id) {
        List<GroupDTO> groupsOfUser = userGroupService.getAllGroupsByUserId(id);
        boolean allGroupsOfUserAreDeleted = true;
        for (GroupDTO group : groupsOfUser) {
            boolean groupOfUserIsDeleted = userGroupService.deleteGroupOfUser(id, group.getId());
            allGroupsOfUserAreDeleted = groupOfUserIsDeleted && allGroupsOfUserAreDeleted;
        }
        return userMapper.delete(id) > 0 && allGroupsOfUserAreDeleted;
    }

    @Override
    public boolean changePassword(UserDTO userDTO, String newPassword) {
        userDTO.setPassword(newPassword);
        int rowsAffected = userMapper.update(convertUserDTOToUserModel(userDTO));
        return rowsAffected > 0;
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
