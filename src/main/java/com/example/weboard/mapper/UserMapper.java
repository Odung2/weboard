package com.example.weboard.mapper;

import com.example.weboard.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    UserDTO getUserById(int id);

    UserDTO getUserByIdOrUserId(UserDTO user);
    int getIdByUserId(String userId);

    String getPasswordById(int id);
    void insertUser(UserDTO user);
    void updateUser(UserDTO user);
    void deleteUser(int id);


}
