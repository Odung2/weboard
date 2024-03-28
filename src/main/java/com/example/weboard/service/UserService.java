package com.example.weboard.service;

import com.example.weboard.mapper.UserMapper;
import com.example.weboard.dto.UserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UserService {
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper){
        this.userMapper=userMapper;
    }

    public UserDTO getUserByIdOrUserId(Integer id){
        UserDTO user = new UserDTO();
        user.setId(id);
        user.setUserId(null);
        user.setPassword(null);
        user.setNickname(null);
        user.setUpdatedBy(null);
        user.setUpdatedAt(null);
        return userMapper.getUserByIdOrUserId(user);
    }
    public UserDTO getUserByIdOrUserId(String userId){
        UserDTO user = new UserDTO();
        user.setId(0);
        user.setUserId(userId);
        user.setPassword(null);
        user.setNickname(null);
        user.setUpdatedBy(null);
        user.setUpdatedAt(null);
        return userMapper.getUserByIdOrUserId(user);
    }

    public String getPasswordById(int id) { return userMapper.getPasswordById(id); }

    public void insertUser(UserDTO user) throws NoSuchAlgorithmException{
        String plainPassword = user.getPassword();
        String sha256Password = plainToSha256(plainPassword);
        user.setPassword(sha256Password);
        userMapper.insertUser(user);
    }

    public void updateUser(UserDTO user) throws NoSuchAlgorithmException{
        String plainPassword = user.getPassword();
        String sha256Password = plainToSha256(plainPassword);
        user.setPassword(sha256Password);
        userMapper.updateUser(user);
    }

    public void deleteUser(int id){
        userMapper.deleteUser(id);
    }

    public String plainToSha256(String plaintext) throws NoSuchAlgorithmException{
        MessageDigest mdSHA256 = MessageDigest.getInstance("SHA-256");
        byte[] sha256Password = mdSHA256.digest(plaintext.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : sha256Password) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }



}
