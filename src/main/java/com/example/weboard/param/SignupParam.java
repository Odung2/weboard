package com.example.weboard.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class SignupParam {

    @NotBlank
    @NotNull
    private String userId;

    @NotBlank
    @NotNull
    private String username;

    @NotBlank
    @NotNull
    @Size(min = 8, max=16, message = "8자 이상 16자 이하의 비밀번호만 가능합니다.")
    private String password;
}
