package com.example.weboard.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokensParam {

    @NotBlank
    @NotNull
    @Schema(description = "Access token", required = true)
    private String accessToken;

    @NotBlank
    @NotNull
    @Schema(description = "refresh token", required = true)
    private String refreshToken;
}
