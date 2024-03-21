package com.example.weboard.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data //geter, setter, required...을 한거번에 설정해주는 유용한 어노테이션
@NoArgsConstructor // 파라미터가 없는 기본 생성자를 생성해줌...
public class UserDTO {
    private int id;
    private String userId;
    private String nickname;
    private String password;
    private Integer updatedBy;
    private LocalDateTime updatedAt;
}
