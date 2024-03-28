package com.example.weboard.mapper;

import com.example.weboard.dto.CommentDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<CommentDTO> getCommentByPostId(int postId);
    void insertComment(CommentDTO comment);

    void updateComment(CommentDTO comment);

    void deleteComment(int commentId);
}
