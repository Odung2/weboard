package com.example.weboard.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class InsertPostParam {

    @NotBlank(message = "제목을 작성해주세요.")
    @Size(min = 1, max=30, message = "제목은 1자 이상 30자 이하만 가능합니다.")
    @Schema(description = "제목")
    private String title;

    @NotBlank(message = "내용을 작성해주세요.")
    @Size(min = 1, max=10000, message = "내용은 1자 이상 1만자 이하만 가능합니다.")
    @Schema(description = "내용")
    private String contents;

    @Nullable
    @Schema(description = "파일 데이터")
    @Size( max=500, message = "파일 데이터 경로는 1자 이상 500자 이하만 가능합니다.")
    private byte[] fileData;
}

