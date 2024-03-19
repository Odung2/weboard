package com.example.weboard.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Comment {
    private int commentId;
    private int postId;
    private int userId;
    private String commentText;
    private int createdBy;
    private String createdAt;
    private Integer updatedBy;
    private String updatedAt;
}
