package com.example.weboard.response;

import com.example.weboard.dto.PostDTO;
import com.example.weboard.service.UserService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

public class PublicPostRes {
    @Schema(description = "포스트 아이디")
    private int postId;
    @Schema(description = "포스트 제목")
    private String title;
    @Schema(description = "사용자 닉네임")
    private String nickname;
    @Schema(description = "조회수")
    private int views;
    @Schema(description = "만든 일시")
    private LocalDateTime createdAt;

    public PublicPostRes(PostDTO post, String nickname){
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.nickname = nickname;
        this.views = post.getViews();
        this.createdAt = post.getCreatedAt();
    }
}
