package com.wuan.attendance.mapper;

import com.wuan.attendance.model.Group;
import com.wuan.attendance.model.User;

import java.util.List;

public interface UserGroupMapper {
    List<Group> getAllGroupsByUserId(Integer userId);
    Group getGroupByUserIdAndGroupId(Integer userId, Integer groupId);
    List<User> getAllUsersByGroupId(Integer groupId);

    boolean deleteSomeGroupsOfUser(Integer userId, List<Integer> groupIds);
}
