package com.example.weboard.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class LoginParam {

    @NotNull
    @NotBlank
    @Schema(description = "사용자 아이디", required = true)
    private String userId;

    @NotBlank
    @NotNull
    @Schema(description = "사용자 비밀번호")
    private String password;
}
