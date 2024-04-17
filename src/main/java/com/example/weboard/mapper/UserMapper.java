package com.example.weboard.mapper;

import com.example.weboard.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper{
    UserDTO getUserById(int id);

    UserDTO getUserByIdOrUserId(UserDTO user);
    int getIdByUserId(String userId);

    String getPasswordById(int id);
    @Override
    void insert(UserDTO user);
    @Override
    void update(UserDTO user);
    @Override
    void delete(int id);


}
