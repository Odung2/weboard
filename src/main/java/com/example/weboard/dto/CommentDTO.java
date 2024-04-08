package com.example.weboard.dto;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@NoArgsConstructor
public class CommentDTO {
    private int commentId;
    private int postId;
    private int userId;
    private String commentText;

    private int createdBy;

    @CreatedDate
    @Column(updatable = false)
    private String createdAt;


    private Integer updatedBy;
    @LastModifiedDate
    private String updatedAt;
}
