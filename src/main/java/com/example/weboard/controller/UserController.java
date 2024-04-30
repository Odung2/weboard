package com.example.weboard.controller;

import com.example.weboard.dto.ApiResponse;
import com.example.weboard.dto.FrkConstants;
import com.example.weboard.dto.TokensDTO;
import com.example.weboard.dto.UserDTO;
import com.example.weboard.exception.PasswordRegexException;
import com.example.weboard.param.LoginParam;
import com.example.weboard.param.SignupParam;
import com.example.weboard.param.TokensParam;
import com.example.weboard.param.UpdateUserParam;
import com.example.weboard.service.AuthService;
import com.example.weboard.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;

import java.security.NoSuchAlgorithmException;

@RequestMapping("/weboard/users")
@RestController
@RequiredArgsConstructor
public class UserController extends BaseController{

    private final UserService userService;
    private final AuthService authService;

    /**
     * 특정 사용자의 정보를 조회합니다.
     * Authorization 헤더를 통해 요청 인증을 수행하고, 사용자 정보를 반환합니다.
     * @param id 조회할 사용자의 ID
     * @return 조회된 사용자 정보와 상태 메시지를 담은 ResponseEntity
     */
    @Operation(summary = "특정 사용자의 정보를 조회", description = "특정 사용자의 정보를 ID로 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(
            @PathVariable int id) {
        return ok(userService.getUser(id));
    }

    @Operation(summary = "refresh token API", description = "access token 만료 시 refresh token을 받습니다.")
    @PostMapping("/refreshToken")
    public ResponseEntity<ApiResponse<String>> refreshTokenAuth(
            @RequestHeader("Authorization") String accessJWT,
            @RequestHeader(value="Refresh-token", defaultValue = "") String refreshJWT
//            @RequestBody @Valid TokensParam tokensParam
            ) throws Exception {
        return ok("새로 발급된 액세스 토큰으로 접속해주세요.", authService.checkRefreshJWTValid(accessJWT, refreshJWT));
    }

    @Operation(summary = "특정 사용자의 정보를 조회", description = "특정 사용자의 정보를 ID로 조회합니다.")
    @GetMapping("/myInfo")
    public ResponseEntity<ApiResponse<UserDTO>> getMyInfo(
            @RequestAttribute("reqId") int id) {
        return ok(userService.getUser(id));
    }

//    @Operation(summary = "특정 사용자의 정보를 조회", description = "특정 사용자의 정보를 ID로 조회합니다.")
//    @PutMapping("/myInfo")
//    public ResponseEntity<ApiResponse<UserDTO>> updateMyInfo(
//            @PathVariable int id) {
//        return ok(userService.getUser(id));
//    }



    /**
     * 새로운 사용자를 등록합니다.
     * @param signupParam 등록할 사용자의 데이터를 담은 DTO
     * @return 등록된 사용자 정보와 상태 메시지를 담은 ResponseEntity
     */
    @Operation(summary = "새로운 사용자 등록", description = "새로운 사용자를 등록합니다.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserDTO>> insertUser(
            @RequestBody @Valid SignupParam signupParam) throws Exception {
        return ok(userService.insertUser(signupParam));
    }

    /**
     * 특정 사용자 정보를 업데이트합니다.
     * Authorization 헤더를 통해 요청 인증을 수행하고, 사용자 정보를 업데이트합니다.
     *
     * @param updateUserParam 업데이트할 사용자의 데이터를 담은 DTO
     * @param id              업데이트할 사용자의 ID
     * @return 업데이트된 사용자 정보와 상태 메시지를 담은 ResponseEntity
     */
    @Operation(summary = "특정 사용자 정보를 업데이트", description = "특정 사용자 정보를 업데이트합니다.")
    @PutMapping("/myinfo")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @RequestAttribute("reqId") int id,
            @RequestBody @Valid UpdateUserParam updateUserParam) throws Exception {
        return ok(userService.updateUser(updateUserParam, id));
    }

    /**
     * 특정 사용자를 삭제합니다.
     * Authorization 헤더를 통해 요청 인증을 수행하고, 사용자를 삭제합니다.
     * @param id 삭제할 사용자의 ID
     * @return 삭제된 사용자의 ID와 상태 메시지를 담은 ResponseEntity
     */
    @Operation(summary = "특정 사용자 삭제", description = "특정 사용자를 삭제합니다.")
    @DeleteMapping("/myinfo")
    public ResponseEntity<ApiResponse<Integer>> deleteUser(
            @RequestAttribute("reqId") int id){
        return ok(userService.deleteUser(id));
    }

    /**
     * 사용자 로그인을 처리합니다.
     * 제공된 사용자 ID와 비밀번호로 인증을 수행하고, 성공 시 JWT 토큰을 반환합니다.
     * @param loginParam 로그인할 userId & password
     * @return 로그인 성공 시 발급된 토큰을 포함한 ResponseEntity
     */
    @Operation(summary = "사용자 로그인", description = "사용자 로그인을 처리합니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokensDTO>> login(
            @RequestBody @Valid LoginParam loginParam) throws Exception{
        return ok(authService.loginAndIssueTokens(loginParam));
    }



}
