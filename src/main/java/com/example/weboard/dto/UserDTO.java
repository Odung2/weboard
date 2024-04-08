package com.example.weboard.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data //getter, setter, required...을 한꺼번에 설정해주는 유용한 어노테이션
@NoArgsConstructor // paramenters 없는 기본 생성자를 생성해줌...
@EntityListeners(AuditingEntityListener.class)
public class UserDTO {
    private int id;
    private String userId;
    private String nickname;
    private String password;

    @CreatedDate
    @Column(updatable = false)
    private String createdAt;

    private Integer updatedBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}

