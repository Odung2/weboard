package com.example.weboard.controller;

import com.example.weboard.dto.UserDTO;
import com.example.weboard.service.AuthService;
import com.example.weboard.service.UserService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RequestMapping("/weboard/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/{id}")
    public UserDTO getUserById(@RequestHeader("Authorization") String jwttoken, @PathVariable int id) {
        if(authService.compareJwtToId(id, jwttoken)){
            return userService.getUserByIdOrUserId(id);
        }
        throw new RuntimeException("본인이 아닌 유저의 정보를 확인할 수 없습니다.");
    }

    @PostMapping("/signup")
    public void insertUser(@RequestBody UserDTO userDTO){
        try {
            userService.insertUser(userDTO);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}")
    public int updateUser(@RequestHeader("Authorization") String jwttoken, @RequestBody UserDTO userDTO, @PathVariable int id){
        if(authService.compareJwtToId(id, jwttoken)){
            try {
                return userService.updateUser(userDTO);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        return 0;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@RequestHeader("Authorization") String jwttoken, @PathVariable int id){
        Integer idFromJwt = authService.getIdFromToken(jwttoken);
        if (idFromJwt!=id){
            return;
        }
        userService.deleteUser(id);
    }

    @PostMapping("/login")
    public String login(@RequestParam(value="userId") String userId, @RequestParam(value="password") String password){
        try {
            return authService.loginAndJwtProvide(userId, password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
