package com.wuan.attendance.service;

import com.wuan.attendance.dto.GroupDTO;
import com.wuan.attendance.dto.UserDTO;

import java.util.List;

public interface UserGroupService {
    List<GroupDTO> getAllGroupsByUserId(Integer userId);

    List<UserDTO> getAllUsersByGroupId(Integer groupId);

    boolean insertUserGroupRelation(Integer userId, Integer groupId);

    boolean deleteGroupOfUser(Integer userId, Integer groupId);
}
