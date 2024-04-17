package com.example.weboard.service;

import com.example.weboard.mapper.CommentMapper;
import com.example.weboard.dto.CommentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;
    private final AuthService authService;
    public List<CommentDTO> getCommentByPostId(int postId){
        return commentMapper.getCommentByPostId(postId);
    }


    public CommentDTO insertComment(CommentDTO comment, int postId, String jwttoken){
        int id = authService.getIdFromToken(jwttoken);

        comment.setPostId(postId);
        comment.setUserId(id);
        commentMapper.insert(comment);
        return comment;
    }

    public CommentDTO updateComment(CommentDTO comment, int commentId){
        comment.setCommentId(commentId);
        commentMapper.update(comment);
        return comment;
    }

    public int deleteComment(int commentId){
        return commentMapper.delete(commentId);
    }
}
