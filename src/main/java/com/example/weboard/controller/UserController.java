package com.example.weboard.controller;

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
    public ResponseEntity<UserDTO> getUserById(@RequestHeader("Authorization") String jwttoken, @PathVariable int id) { // interceptor 를 이용 Authorization을 매번 체크? ->
        if(authService.compareJwtToId(id, jwttoken)){ // 순서.. param RESPONSE TYPE 정의 해서 에러코드 -메시지- 데이터 0 " 메시지 없음" "data 안에 해당되는 스트럭쳐"
            return userService.getUserByIdOrUserId(id);
        }
        throw new RuntimeException("본인이 아닌 유저의 정보를 확인할 수 없습니다."); // 에러코드 에러메시지 status http status 유저 200 정상 런타임 말고 ... RETURN DATA TYPE을 만들기. 응답 값도.  데이터 스트럭쳐 에러 코드: "INTEGER" 에러코드 아니면 "0" " OKAY" // 에러면 "100 " "200" "300" .... 번호에 대해 구간을 정의. 에러코드만 봐도..
    }

    @PostMapping("/signup")
    public ResponseEntity<String> insertUser(@RequestBody UserDTO userDTO){
        return userService.insertUser(userDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@RequestHeader("Authorization") String jwttoken, @RequestBody UserDTO userDTO, @PathVariable int id){
        if(authService.compareJwtToId(id, jwttoken)){
            return userService.updateUser(userDTO);
        }
        return ResponseEntity.status(500).body("INTERNET_SERVER_ERROR: 유저 업데이트 중 에러가 발생했습니다.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String jwttoken, @PathVariable int id){
        Integer idFromJwt = authService.getIdFromToken(jwttoken);
        if (idFromJwt!=id){
            return ResponseEntity.status(400).body("BAD_REQUEST: 본인이 아닌 유저의 회원정보 삭제는 불가능합니다.");
        }
        return userService.deleteUser(id);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam(value="userId") String userId, @RequestParam(value="password") String password){
            return authService.loginAndJwtProvide(userId, password);
    }

}
