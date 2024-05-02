package com.example.weboard.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class CommentRes {
    @Schema(description = "게시물 ID")
    public int postId;
    @Schema(description = "사용자 닉네임")
    public String nickname;
    @Schema(description = "댓글 내용")
    public String commentText;
    @Schema(description = "만든 일시")
    public LocalDateTime createdAt;
    @Schema(description = "최종 변경 일시")
    public LocalDateTime updatedAt;
}
