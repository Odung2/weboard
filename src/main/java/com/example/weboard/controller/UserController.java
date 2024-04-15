package com.example.weboard.controller;

import com.example.weboard.dto.ApiResponse;
import com.example.weboard.dto.UserDTO;
import com.example.weboard.service.AuthService;
import com.example.weboard.service.UserService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse> getUserById(@RequestHeader("Authorization") String jwttoken, @PathVariable int id) { // interceptor 를 이용 Authorization을 매번 체크? ->
        return userService.getUserByIdOrUserId(id);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> insertUser(@RequestBody UserDTO userDTO){
        return userService.insertUser(userDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUser(@RequestHeader("Authorization") String jwttoken, @RequestBody UserDTO userDTO, @PathVariable int id){
        return userService.updateUser(userDTO, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@RequestHeader("Authorization") String jwttoken, @PathVariable int id){
        return userService.deleteUser(id);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestParam(value="userId") String userId, @RequestParam(value="password") String password){
            return authService.loginAndJwtProvide(userId, password);
    }

}
