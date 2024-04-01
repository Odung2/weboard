package com.example.weboard.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data //getter, setter, required...을 한꺼번에 설정해주는 유용한 어노테이션
@NoArgsConstructor // paramenters 없는 기본 생성자를 생성해줌...
public class UserDTO {
    private int id;
    private String userId;
    private String nickname;
    private String password;
    private String createdAt;
    private Integer updatedBy;
    private LocalDateTime updatedAt;
}

