package com.example.weboard.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class LoginParam {

    @NotNull
    @NotBlank
    private String userId;

    @NotBlank
    @NotNull
    private String password;
}
