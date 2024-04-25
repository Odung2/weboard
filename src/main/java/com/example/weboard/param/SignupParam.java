package com.example.weboard.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class SignupParam {

    @NotBlank
    @NotNull
    @Schema(description = "사용자 아이디", required = true)
    private String userId;

    @NotBlank
    @NotNull
    @Schema(description = "사용자 닉네임")
    private String nickname;

    @NotBlank
    @NotNull
    @Size(min = 8, max=16, message = "8자 이상 16자 이하의 비밀번호만 가능합니다.")
    @Schema(description = "사용자 비밀번호")
    private String password;
}
