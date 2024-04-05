package com.example.weboard.service;

import com.example.weboard.mapper.UserMapper;
import com.example.weboard.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    public ResponseEntity<UserDTO> getUserByIdOrUserId(int param){ //user
        UserDTO userparam = new UserDTO();
        userparam.setId(param);
        UserDTO user = userMapper.getUserByIdOrUserId(userparam);
        return ResponseEntity.status(200).body(user);
    }
    public ResponseEntity<UserDTO> getUserByIdOrUserId(String param){ //user
        UserDTO userparam = new UserDTO();
        userparam.setUserId(param);
        UserDTO user = userMapper.getUserByIdOrUserId(userparam);
        return ResponseEntity.status(200).body(user);
    }

    public String getPasswordById(int id) {
        return userMapper.getPasswordById(id);
    }

    public ResponseEntity<String> insertUser(UserDTO user) {
        String plainPassword = user.getPassword();
        String sha256Password = plainToSha256(plainPassword);
        user.setPassword(sha256Password);
        userMapper.insertUser(user);
        return ResponseEntity.status(201).body("성공적으로 유저가 생성되었습니다.");
    }

    public ResponseEntity<String> updateUser(UserDTO user) {
        Integer count = 0;
        if(user.getUserId()!=null){
            count += 1;
        }
        if(user.getNickname()!=null){
            count += 1;
        }
        if(user.getPassword()!=null){
            count += 1;
        }

        String plainPassword = user.getPassword();
        if(plainPassword!=null){
            String sha256Password = plainToSha256(plainPassword);
            user.setPassword(sha256Password);
        }
        user.setUpdatedAt(LocalDateTime.now());
        String message = count.toString() + "개의 유저 속성이 업데이트 되었습니다.";
        return ResponseEntity.status(200).body(message);
    }

    public ResponseEntity<String> deleteUser(int id){
        userMapper.deleteUser(id);
        return ResponseEntity.status(200).body("성공적으로 유저 정보가 삭제되었습니다.");
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
