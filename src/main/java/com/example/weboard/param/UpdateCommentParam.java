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

    @NotNull
    @NotBlank
    @Schema(description = "게시물 ID", required = true)
    private int postId;

    @NotNull
    @NotBlank
    @Schema(description = "사용자 ID", required = true)
    private int userId;

    @NotNull(message = "댓글 내용을 작성해주세요.")
    @NotBlank(message = "댓글 내용을 작성해주세요.")
    @Schema(description = "댓글 내용", required = true)
    private String commentText;
}
