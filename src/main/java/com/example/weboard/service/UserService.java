package com.example.weboard.service;

import com.example.weboard.mapper.UserMapper;
import com.example.weboard.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper){
        this.userMapper=userMapper;
    }

    public User getUserById(int userId){
        return userMapper.getUserById(userId);
    }

    public void insertUser(User user){
        userMapper.insertUser(user);
    }

    public void updateUser(User user){
        userMapper.updateUser(user);
    }

    public void deleteUser(int userId){
        userMapper.deleteUser(userId);
    }

}
