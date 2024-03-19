package com.example.weboard.mapper;

import com.example.weboard.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User getUserById(int userId);
    void insertUser(User user);
    void updateUser(User user);
    void deleteUser(int userId);
}
