package com.example.weboard.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
//@Builder
//@AllArgsConstructor
//@RedisHash(value="token", timeToLive = 7200000)
@Schema(description = "토큰 정보를 나타내는 DTO")
public class TokensDTO {

    @NotNull
    @NotBlank
    @Id
    @Schema(description = "액세스 토큰", required = true)
    private String accessToken;

    @Schema(description = "리프레시 토큰", required = true)
    private String refreshToken;
}
