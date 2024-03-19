package com.example.weboard.service;

import com.example.weboard.mapper.CommentMapper;
import com.example.weboard.model.Comment;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentMapper commentMapper;

    public CommentService(CommentMapper commentMapper){
        this.commentMapper=commentMapper;
    }

    public Comment getCommentByPostId(int postId){
        return commentMapper.getCommentByPostId(postId);
    }

    public void insertComment(Comment comment){
        commentMapper.insertComment(comment);
    }

    public void updateComment(Comment comment){
        commentMapper.updateComment(comment);
    }

    public void deleteComment(int commentId){
        commentMapper.deleteComment(commentId);
    }
}
