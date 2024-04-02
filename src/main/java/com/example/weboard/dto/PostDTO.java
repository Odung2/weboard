package com.example.weboard.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostDTO {
    private int postId;
    private String title;
    private int views;
    private String contents;
    private byte[] fileData;
    private int createdBy;
    private String createdAt;
    private Integer updatedBy;
    private String updatedAt;
}
