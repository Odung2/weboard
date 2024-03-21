package com.example.weboard.controller;

import com.example.weboard.dto.UserDTO;
import com.example.weboard.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RequestMapping("/weboard/users")
@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService=userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
        UserDTO user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Void> insertUser(@RequestBody UserDTO user){
        try {
            userService.insertUser(user);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable int id, @RequestBody UserDTO user){
        user.setId(id);
        try {
            userService.updateUser(user);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id){
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
