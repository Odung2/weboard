package com.example.weboard.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Schema(description = "게시물 정보를 담는 DTO")
public class PostDTO {

    @Schema(description = "게시물 ID", required = true)
    private int postId;

    @Schema(description = "제목", required = true)
    private String title;

    @Schema(description = "조회수", required = true)
    private int views;

    @Schema(description = "내용")
    private String contents;

    @Schema(description = "파일 데이터")
    private byte[] fileData;

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
