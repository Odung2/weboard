package com.example.weboard.mapper;

import com.example.weboard.dto.CommentDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {
    CommentDTO getCommentByPostId(int postId);
    void insertComment(CommentDTO comment);

    void updateComment(CommentDTO comment);

    void deleteComment(int commentId);
}
