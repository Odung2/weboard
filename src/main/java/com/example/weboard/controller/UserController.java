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
     * 사용자 공공(public) 정보 조회, credential 필요 없음
     * @param id
     * @return
     */
    @Operation(summary = "사용자의 공공(public)정보를 조회", description = "특정 사용자의 공공정보를 ID로 조회합니다.")
    @GetMapping("/public/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(
            @PathVariable int id) {
        return ok(userService.getUser(id));
    }

    /**
     * 로그인
     * @param loginParam [userId, password]
     * @return [액세스 토큰, 리프레시 토큰]
     * @throws Exception
     */
    @Operation(summary = "사용자 로그인", description = "사용자 로그인을 처리합니다.")
    @PostMapping("/public/login")
    public ResponseEntity<ApiResponse<TokensDTO>> login(
            @RequestBody @Valid LoginParam loginParam) throws Exception{
        return ok(authService.loginAndIssueTokens(loginParam));
    }

    /**
     * 새 액세스 토큰 발급(액세스 토큰 만료 && 리프레시 토큰 유효 시)
     * @param accessJWT 만료된 액세스 토큰
     * @param refreshJWT 유효한 리프레시 토큰
     * @return 새 액세스 토큰 발급
     * @throws Exception 캐시 Not Found / 리프레시 만료 / 액세스 유효
     */
    @Operation(summary = "refresh token API", description = "access token 만료 시 refresh token을 받습니다.")
    @PostMapping("/public/refresh-token")
    public ResponseEntity<ApiResponse<String>> refreshTokenAuth(
            @RequestHeader("Authorization") String accessJWT,
            @RequestHeader(value="Refresh-token", defaultValue = "") String refreshJWT
            ) throws Exception {
        return ok("새로 발급된 액세스 토큰으로 접속해주세요.", authService.issueNewAccessToken(accessJWT, refreshJWT));
    }

    /**
     * 회원가입
     * @param signupParam 등록할 사용자의 데이터를 담은 DTO(userId, nickname, password)
     * @return user 등록된 유저 정보
     */
    @Operation(summary = "새로운 사용자 등록", description = "새로운 사용자를 등록합니다.")
    @PostMapping("/public/signup")
    public ResponseEntity<ApiResponse<UserDTO>> insertUser(
            @RequestBody @Valid SignupParam signupParam) throws Exception {
        return ok(userService.insertUser(signupParam));
    }

    /**
     * 개인정보 조회, 액세스 토큰을 통해 인증된 사용자의 개인정보 조회 요청 처리
     * @param id 액세스 토큰
     * @return user
     */
    @Operation(summary = "개인정보 조회", description = "사용자의 개인정보를 액세스 토큰을 통해 조회합니다.")
    @GetMapping("/my-info")
    public ResponseEntity<ApiResponse<UserDTO>> getMyInfo(
            @RequestAttribute("reqId") int id) {
        return ok(userService.getUser(id));
    }

    /**
     * 개인정보 수정, 액세스 토큰을 통해 인증된 사용자의 개인정보 수정 요청 처리
     * @param id
     * @param updateUserParam [nickname, password], 둘 다 nullable
     * @return
     * @throws Exception Password 양식 Exception, 암호화 알고리즘 Exception
     */
    @Operation(summary = "개인정보 수정", description = "개인정보(nickname || password)를 수정할 수 있습니다.")
    @PutMapping("/my-info")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @RequestAttribute("reqId") int id,
            @RequestBody @Valid UpdateUserParam updateUserParam) throws Exception {
        return ok(userService.updateUser(updateUserParam, id));
    }

    /**
     * 회원 탈퇴 (DB에 저장된 개인정보를 삭제합니다.)
     * @param id 본인의 id
     * @return
     */
    @Operation(summary = "특정 사용자 삭제", description = "특정 사용자를 삭제합니다.")
    @DeleteMapping("/my-info")
    public ResponseEntity<ApiResponse<Integer>> deleteUser(
            @RequestAttribute("reqId") int id){
        return ok(userService.deleteUser(id));
    }

}
