package com.example.weboard.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PostViewBO {
    private PostDTO post;
    private List<CommentDTO> comment;
}
