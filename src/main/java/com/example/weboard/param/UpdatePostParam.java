package com.example.weboard.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class UpdatePostParam {

    @NotBlank(message = "제목을 작성해주세요.")
    @NotNull(message = "제목을 작성해주세요.")
    private String title;

    @NotBlank(message = "내용을 작성해주세요.")
    @NotNull(message = "내용을 작성해주세요.")
    private String contents;

    private byte[] fileData;
}

