package com.example.weboard.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateUserParam {
    @Nullable
    @Schema(description = "사용자 닉네임")
    private String nickname;

    @Nullable
    @Size(min = 8, max=16, message = "8자 이상 16자 이하의 비밀번호만 가능합니다.")
    @Schema(description = "사용자 비밀번호")
    private String password;
}
