package com.example.weboard.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Schema(description = "사용자 정보를 나타내는 DTO")
public class UserDTO extends BaseDTO{

    @Schema(description = "사용자 ID", required = true)
    private int id;

    @Schema(description = "사용자 아이디", required = true)
    private String userId;

    @Schema(description = "사용자 닉네임")
    private String nickname;

    @NotBlank
    @NotNull
    @Size(min = 8, max=16, message = "8자 이상 16자 이하의 비밀번호만 가능합니다.")
    @Schema(description = "사용자 비밀번호")
    private String password;
    //regex는 12자 미만, 12자 이상 조건이 달라서 조건 체크로 남겨둠.

    @Schema(description = "마지막 로그인 일자")
    @LastModifiedDate
    private Date lastLogin;

    @Schema(description = "로그인 실패 횟수")
    private int loginFail;

    @Schema(description = "계정 잠금 여부")
    private int isLocked;

    @Schema(description = "마지막 비밀번호 변경 일자")
    @LastModifiedDate
    private Date lastPwUpdated;

    @Schema(description = "로그인 잠금 일자")
    @LastModifiedDate
    private Date loginLocked;
}
