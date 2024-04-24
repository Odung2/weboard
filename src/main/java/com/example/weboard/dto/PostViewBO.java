package com.example.weboard.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Data
@NoArgsConstructor
@Schema(description = "게시물 및 해당 게시글의 댓글 정보를 담는 비즈니스 객체")
public class PostViewBO {

    @Schema(description = "게시물 정보")
    private PostDTO post;

    @Schema(description = "댓글 목록")
    private List<CommentDTO> comment;
}
