package com.wuan.attendance.mapper;

import com.wuan.attendance.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> findAll();
    User findById(Integer id);
    User findByEmail(String email);
    int insert(User user);
    int update(User user);
    int delete(Integer id);
    // Other custom methods if needed
}
