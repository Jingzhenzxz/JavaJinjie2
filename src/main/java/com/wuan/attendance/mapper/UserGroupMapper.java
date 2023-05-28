package com.wuan.attendance.mapper;

import java.util.List;

public interface UserGroupMapper {
    List<Integer> getAllGroupIdsByUserId(Integer userId);

    List<Integer> getAllUserIdsByGroupId(Integer groupId);

    int insertUserGroupRelation(Integer userId, Integer groupId);

    int deleteGroupOfUser(Integer userId, Integer groupId);
}
