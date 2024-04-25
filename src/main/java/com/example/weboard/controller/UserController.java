package com.example.weboard.controller;

import com.example.weboard.dto.ApiResponse;
import com.example.weboard.dto.FrkConstants;
import com.example.weboard.dto.TokensDTO;
import com.example.weboard.dto.UserDTO;
import com.example.weboard.service.AuthService;
import com.example.weboard.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RequestMapping("/weboard/users")
@RestController
@RequiredArgsConstructor
public class UserController extends BaseController{

    private final UserService userService;
    private final AuthService authService;

    /**
     * 특정 사용자의 정보를 조회합니다.
     * Authorization 헤더를 통해 요청 인증을 수행하고, 사용자 정보를 반환합니다.
     * @param jwttoken 인증을 위한 JWT 토큰
     * @param id 조회할 사용자의 ID
     * @return 조회된 사용자 정보와 상태 메시지를 담은 ResponseEntity
     */
    @Operation(summary = "특정 사용자의 정보를 조회", description = "특정 사용자의 정보를 ID로 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(
            @RequestHeader("Authorization") String jwttoken,
            @PathVariable int id) {
        userService.getUserByIdOrUserId(id);
        return ok(FrkConstants.getUser, userService.getUserByIdOrUserId(id));
    }

    /**
     * 새로운 사용자를 등록합니다.
     * @param userDTO 등록할 사용자의 데이터를 담은 DTO
     * @return 등록된 사용자 정보와 상태 메시지를 담은 ResponseEntity
     */
    @Operation(summary = "새로운 사용자 등록", description = "새로운 사용자를 등록합니다.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserDTO>> insertUser(
            @RequestBody @Valid UserDTO userDTO) throws Exception {
        return ok(FrkConstants.insertUser, userService.insertUser(userDTO));
    }

    /**
     * 특정 사용자 정보를 업데이트합니다.
     * Authorization 헤더를 통해 요청 인증을 수행하고, 사용자 정보를 업데이트합니다.
     * @param jwttoken 인증을 위한 JWT 토큰
     * @param userDTO 업데이트할 사용자의 데이터를 담은 DTO
     * @param id 업데이트할 사용자의 ID
     * @return 업데이트된 사용자 정보와 상태 메시지를 담은 ResponseEntity
     */
    @Operation(summary = "특정 사용자 정보를 업데이트", description = "특정 사용자 정보를 업데이트합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @RequestHeader("Authorization") String jwttoken,
            @RequestBody UserDTO userDTO,
            @PathVariable int id){
        return ok(FrkConstants.updateUser, userService.updateUser(userDTO, id));
    }

    /**
     * 특정 사용자를 삭제합니다.
     * Authorization 헤더를 통해 요청 인증을 수행하고, 사용자를 삭제합니다.
     * @param jwttoken 인증을 위한 JWT 토큰
     * @param id 삭제할 사용자의 ID
     * @return 삭제된 사용자의 ID와 상태 메시지를 담은 ResponseEntity
     */
    @Operation(summary = "특정 사용자 삭제", description = "특정 사용자를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Integer>> deleteUser(
            @RequestHeader("Authorization") String jwttoken,
            @PathVariable int id){
        return ok(FrkConstants.deleteUser, userService.deleteUser(id));
    }

    /**
     * 사용자 로그인을 처리합니다.
     * 제공된 사용자 ID와 비밀번호로 인증을 수행하고, 성공 시 JWT 토큰을 반환합니다.
     * @param userId 로그인할 사용자의 ID
     * @param password 로그인할 사용자의 비밀번호
     * @return 로그인 성공 시 발급된 토큰을 포함한 ResponseEntity
     */
    @Operation(summary = "사용자 로그인", description = "사용자 로그인을 처리합니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokensDTO>> login(
            @Parameter(description = "로그인할 사용자의 ID", required = true)
            @RequestParam(value="userId") String userId,
            @Parameter(description = "로그인할 사용자의 비밀번호", required = true)
            @RequestParam(value="password") String password) throws Exception {
        return ok(FrkConstants.successLogin, authService.loginAndJwtProvide(userId, password));
    }

}
