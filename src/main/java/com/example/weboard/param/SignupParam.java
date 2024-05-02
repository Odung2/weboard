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
public class SignupParam {

    @NotBlank(message = "아이디를 작성해주세요.")
    @Size(min = 2, max=16, message = "아이디는 2자 이상 16자 이하만 가능합니다.")
    @Schema(description = "사용자 아이디")
    private String userId;

    @NotBlank(message = "닉네임을 작성해주세요.")
    @Size(min = 1, max=20, message = "닉네임은 1자 이상 20자 이하만 가능합니다.")
    @Schema(description = "사용자 닉네임")
    private String nickname;

    @NotBlank(message = "비밀먼호를 작성해주세요.")
    @Size(min = 8, max=16, message = "8자 이상 16자 이하의 비밀번호만 가능합니다.")
    @Schema(description = "사용자 비밀번호")
    private String password;
}
