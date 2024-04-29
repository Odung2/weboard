package com.example.weboard.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
//@Builder
//@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Schema(description = "게시물 정보를 담는 DTO")
public class PostDTO extends BaseDTO{

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


}
