package com.example.weboard.service;

import com.example.weboard.mapper.UserMapper;
import com.example.weboard.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    public UserDTO getUserByIdOrUserId(int param){ //user
        UserDTO userparam = new UserDTO();
        userparam.setId(param);
        return userMapper.getUserByIdOrUserId(userparam);
    }
    public UserDTO getUserByIdOrUserId(String param){ //user
        UserDTO userparam = new UserDTO();
        userparam.setUserId(param);
        return userMapper.getUserByIdOrUserId(userparam);
    }

    public String getPasswordById(int id) {
        return userMapper.getPasswordById(id);
    }

    public UserDTO insertUser(UserDTO user) {
        String plainPassword = user.getPassword();
        String sha256Password = plainToSha256(plainPassword);
        user.setPassword(sha256Password);
        userMapper.insert(user);
        return user;
    }

    public UserDTO updateUser(UserDTO user, int id) {
        user.setId(id);

//        Integer count = 0;
//        if(user.getUserId()!=null){ //변경 불가능
//            count += 1;
//        }
//        if(user.getNickname()!=null){
//            count += 1;
//        }
//        if(user.getPassword()!=null){
//            count += 1;
//        }

        String plainPassword = user.getPassword();
        if(plainPassword!=null){
            String sha256Password = plainToSha256(plainPassword);
            user.setPassword(sha256Password);
        }
        user.setUpdatedAt(LocalDateTime.now());

        userMapper.update(user);
        return user;
    }

    public int deleteUser(int id){
        return userMapper.delete(id);
    }

    public String plainToSha256(String plaintext) {
        MessageDigest mdSHA256 = null;
        try {
            mdSHA256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
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
