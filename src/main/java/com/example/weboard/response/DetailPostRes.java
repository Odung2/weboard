package com.example.weboard.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class DetailPostRes {

    @Schema(description = "게시물 ID")
    private int postId;
    @Schema(description = "제목")
    private String title;
    @Schema(description = "사용자 닉네임")
    private String nickname;
    @Schema(description = "조회수")
    private int views;
    @Schema(description = "내용")
    private String contents;
    @Schema(description = "파일 데이터")
    private byte[] fileData;
    @Schema(description = "댓글 리스트")
    private List<CommentRes> comments;

}
