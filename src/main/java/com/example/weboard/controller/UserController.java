package com.example.weboard.controller;

import com.example.weboard.dto.ApiResponse;
import com.example.weboard.dto.FrkConstants;
import com.example.weboard.dto.UserDTO;
import com.example.weboard.service.AuthService;
import com.example.weboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/weboard/users")
@RestController
@RequiredArgsConstructor
public class UserController extends BaseController{

    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@RequestHeader("Authorization") String jwttoken, @PathVariable int id) { // interceptor 를 이용 Authorization을 매번 체크? ->
        userService.getUserByIdOrUserId(id);
        return ok(FrkConstants.getUser, userService.getUserByIdOrUserId(id));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserDTO>> insertUser(@RequestBody UserDTO userDTO){
        return ok(FrkConstants.insertUser, userService.insertUser(userDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@RequestHeader("Authorization") String jwttoken, @RequestBody UserDTO userDTO, @PathVariable int id){
        return ok(FrkConstants.updateUser, userService.updateUser(userDTO, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Integer>> deleteUser(@RequestHeader("Authorization") String jwttoken, @PathVariable int id){
        return ok(FrkConstants.deleteUser, userService.deleteUser(id));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestParam(value="userId") String userId, @RequestParam(value="password") String password){
            return ok(FrkConstants.successLogin, authService.loginAndJwtProvide(userId, password));
    }

}
