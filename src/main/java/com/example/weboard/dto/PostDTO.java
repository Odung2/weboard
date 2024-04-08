package com.example.weboard.dto;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@NoArgsConstructor
public class PostDTO {
    private int postId;
    private String title;
    private int views;
    private String contents;
    private byte[] fileData;
    private int createdBy;

    @CreatedDate
    @Column(updatable = false)
    private String createdAt;

    private Integer updatedBy;

    @LastModifiedDate
    private String updatedAt;
}
