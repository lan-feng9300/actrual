package com.example.demo.mapper;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CacheMapper {

    @Select("select * from user_info")
    List<User> selectAllUserInfo();

    @Update("UPDATE user_info set user_name = #{userName} WHERE user_id = 1")
    Integer updateUserInfo(String userName);
}
