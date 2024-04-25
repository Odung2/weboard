package com.example.weboard.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class InsertCommentParam {

    @NotNull
    @NotBlank
    private int postId;

    @NotNull
    @NotBlank
    private int userId;

    @NotNull(message = "댓글 내용을 작성해주세요.")
    @NotBlank(message = "댓글 내용을 작성해주세요.")
    private String commentText;
}
