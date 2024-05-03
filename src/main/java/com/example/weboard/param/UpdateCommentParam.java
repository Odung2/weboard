package com.example.weboard.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateCommentParam {

//    @NotBlank(message = "게시물 ID를 작성해주세요.")
//    @Schema(description = "게시물 ID")
//    private int postId;
//
//    @NotBlank(message = "사용자 ID를 작성해주세요.")
//    @Schema(description = "사용자 ID")
//    private int userId;

    @NotBlank(message = "댓글 내용을 작성해주세요.")
    @Schema(description = "댓글 내용")
    private String commentText;
}
