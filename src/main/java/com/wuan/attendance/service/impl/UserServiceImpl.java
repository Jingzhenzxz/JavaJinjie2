package com.wuan.attendance.service.impl;

import com.wuan.attendance.dto.GroupDTO;
import com.wuan.attendance.dto.UserDTO;
import com.wuan.attendance.exception.UserException;
import com.wuan.attendance.mapper.UserMapper;
import com.wuan.attendance.model.Group;
import com.wuan.attendance.model.User;
import com.wuan.attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

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
        return userMapper.insert(convertUserDTOToUserModel(userDTO)) > 0;
    }

    @Override
    public boolean update(UserDTO userDTO) {
        return userMapper.update(convertUserDTOToUserModel(userDTO)) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        return userMapper.delete(id) > 0;
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
