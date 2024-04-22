package com.example.weboard.dto;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
//@RedisHash(value="token", timeToLive = 7200000)
public class TokensDTO {
    @Id
    private String accessToken;
    private String refreshToken;
}
