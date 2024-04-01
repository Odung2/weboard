package com.example.weboard.controller;

import com.example.weboard.dto.UserDTO;
import com.example.weboard.service.AuthService;
import com.example.weboard.service.UserService;
import jakarta.websocket.server.PathParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RequestMapping("/weboard/users")
@RestController
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService){
        this.userService=userService;
        this.authService = authService;
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable int id) {
        return userService.getUserByIdOrUserId(id);
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
    public void updateUser(@RequestHeader("Authorization") String jwttoken, @RequestBody UserDTO userDTO, @PathVariable int id){
        Integer idFromJwt = authService.getIdFromToken(jwttoken);
        userDTO.setId(id);
        if (idFromJwt!=id){
            return;
        }
        try {
            userService.updateUser(userDTO);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id){
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
