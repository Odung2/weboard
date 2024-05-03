package com.example.weboard.response;

import com.example.weboard.dto.UserDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.security.PublicKey;
import java.time.LocalDateTime;
@Data
public class PublicUserInfoRes {
    @Schema(description = "사용자 닉네임")
    private String nickname;

    @Schema(description = "만든 일시")
    private LocalDateTime createdAt;

    public PublicUserInfoRes(UserDTO user) {
        this.nickname = user.getNickname();
        this.createdAt = user.getCreatedAt();
    }
}
