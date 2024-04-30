package com.example.weboard.mapper;

import com.example.weboard.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper{
    UserDTO getUserByIdOrUserId(int id);
    UserDTO getUserByIdOrUserId(UserDTO user);
    int getIdByUserId(String userId);
    String getPasswordById(int id);
    int addLoginFailCount(int id);
    int resetLoginFailCount(int id);

    int resetLoginLocked(int id);
    int lockUnlockUser(@Param("id") int id, @Param("isLocked") int isLocked);
    int updateLoginLocked(UserDTO user);
    @Override
    int insert(UserDTO user);
    @Override
    int update(UserDTO user);
    @Override
    int delete(int id);


}
