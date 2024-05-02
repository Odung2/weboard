package com.example.weboard.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class InsertCommentParam {

    @NotBlank(message = "댓글 내용을 작성해주세요.")
    private String commentText;
}
