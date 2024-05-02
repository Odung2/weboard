package com.example.weboard.response;

import io.swagger.v3.oas.annotations.media.Schema;

public class PublicUserInfoRes {
    @Schema(description = "사용자 닉네임")
    private String nickname;

    @Schema(description = "만든 일시")
    private String createdAt;
}
