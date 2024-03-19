package com.example.weboard.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Post {
    private int postId;
    private String title;
    private int createdBy;
//    private String createdAt;
    private Integer updatedBy;
    private String updatedAt;
    private int views;
    private String contentText;
    private byte[] fileData;
}
