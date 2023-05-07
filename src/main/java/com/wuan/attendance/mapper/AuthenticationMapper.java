package com.wuan.attendance.mapper;

import com.wuan.attendance.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AuthenticationMapper {
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);
}