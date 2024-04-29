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
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Schema(description = "댓글 정보를 담는 DTO")
public class CommentDTO extends BaseDTO {

    @Schema(description = "댓글 ID", required = true)
    public int commentId;

    @Schema(description = "게시물 ID", required = true)
    public int postId;

    @Schema(description = "사용자 ID", required = true)
    public int userId;

    @Schema(description = "댓글 내용", required = true)
    public String commentText;

}
