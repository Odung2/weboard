package com.example.weboard.mapper;

import com.example.weboard.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper{
    UserDTO getUserById(int id);
    UserDTO getUserByIdOrUserId(UserDTO user);
    int getIdByUserId(String userId);
    String getPasswordById(int id);
    int addLoginFailCount(int id);
    int resetLoginFailCount(int id);

    int resetLoginLocked(int id);
    int lockUnlockUser(int id, int lockUser);
    int updateLoginLocked(UserDTO user);
    @Override
    int insert(UserDTO user);
    @Override
    int update(UserDTO user);
    @Override
    int delete(int id);


}
