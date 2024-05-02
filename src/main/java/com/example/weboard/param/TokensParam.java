package com.example.weboard.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokensParam {

    @NotBlank(message = "액세스 토큰을 넣어주세요.")
    @Schema(description = "Access token")
    private String accessToken;

    @NotBlank(message = "리프레시 토큰을 넣어주세요.")
    @Schema(description = "refresh token")
    private String refreshToken;
}
