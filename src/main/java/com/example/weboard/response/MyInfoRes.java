package com.example.weboard.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class MyInfoRes {
    @Schema(description = "사용자 아이디")
    private String userId;
    @Schema(description = "사용자 닉네임")
    private String nickname;
    @Schema(description = "만든 일시")
    private LocalDateTime createdAt;
    @Schema(description = "마지막 변경 일시")
    private LocalDateTime updatedAt;
}
