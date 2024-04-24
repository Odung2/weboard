package com.example.weboard.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Schema(description = "사용자 정보를 나타내는 DTO")
public class UserDTO {

    @Schema(description = "사용자 ID", required = true)
    private int id;

    @Schema(description = "사용자 아이디", required = true)
    private String userId;

    @Schema(description = "사용자 닉네임")
    private String nickname;

    @Schema(description = "사용자 비밀번호")
    private String password;

    @Schema(description = "생성일", readOnly = true)
    @CreatedDate
    @Column(updatable = false)
    private String createdAt;

    @Schema(description = "최종 수정한 사용자의 ID")
    private Integer updatedBy;

    @Schema(description = "최종 수정 일자")
    @LastModifiedDate
    private LocalDateTime updatedAt;

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
