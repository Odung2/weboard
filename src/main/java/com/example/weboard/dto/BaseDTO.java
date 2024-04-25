package com.example.weboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

public class BaseDTO {

    @Schema(description = "작성자 ID", required = true)
    private int createdBy;

    @CreatedDate
    @Column(updatable = false)
    @Schema(description = "작성일", readOnly = true)
    private String createdAt;


    @Schema(description = "최종 수정한 사용자의 ID")
    private Integer updatedBy;

    @LastModifiedDate
    @Schema(description = "최종 수정일")
    private String updatedAt;

}
