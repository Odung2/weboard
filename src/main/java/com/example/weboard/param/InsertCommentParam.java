package com.example.weboard.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class InsertCommentParam {

    @NotBlank(message = "댓글 내용을 작성해주세요.")
    @Size(min = 1, max=500, message = "댓글은 1자 이상 500자 이하만 가능합니다.")
    private String commentText;
}
