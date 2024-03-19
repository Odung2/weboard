package com.example.weboard.mapper;

import com.example.weboard.model.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {
    Comment getCommentByPostId(int postId);

    void insertComment(Comment comment);

    void updateComment(Comment comment);

    void deleteComment(int commentId);
}
