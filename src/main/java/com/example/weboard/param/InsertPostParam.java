package com.example.weboard.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class InsertPostParam {

//    @NotBlank
//    @NotNull
//    @Schema(description = "게시물 ID", required = true)
//    private int postId;

    @NotBlank(message = "제목을 작성해주세요.")
    @NotNull(message = "제목을 작성해주세요.")
    @Schema(description = "제목", required = true)
    private String title;

    @NotBlank(message = "내용을 작성해주세요.")
    @NotNull(message = "내용을 작성해주세요.")
    @Schema(description = "내용")
    private String contents;

    @Schema(description = "파일 데이터")
    private byte[] fileData;
}

