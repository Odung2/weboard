package com.example.weboard.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
//@Builder
//@SuperBuilder
//@AllArgsConstructor
//@SuperBuilder(builderMethodName = "updateUser")
//@Entity
//@EntityListeners(AuditingEntityListener.class)
//@EntityListeners(UpdateEntityListener.class)
@Schema(description = "사용자 정보를 나타내는 DTO")
public class UserDTO extends BaseDTO{

    @Id
    @Schema(description = "사용자 ID")
    private int id;

    @Schema(description = "사용자 아이디")
    private String userId;

    @Schema(description = "사용자 닉네임")
    private String nickname;

    @Schema(description = "사용자 비밀번호")
    private String password;
    //regex는 12자 미만, 12자 이상 조건이 달라서 조건 체크로 남겨둠.

    @Schema(description = "마지막 로그인 일자")
    @LastModifiedDate
    private LocalDateTime lastLoginAt;

    @Schema(description = "로그인 실패 횟수")
    private int loginFailCount;

    @Schema(description = "계정 잠금 여부")
    private int isLocked;

    @Schema(description = "마지막 비밀번호 변경 일자")
    @LastModifiedDate
    private LocalDateTime pwUpdatedAt;

    @Schema(description = "로그인 잠금 일자")
    @LastModifiedDate
    private LocalDateTime loginLockedAt;

}
