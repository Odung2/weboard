package com.example.weboard.service;

import com.example.weboard.mapper.CommentMapper;
import com.example.weboard.dto.CommentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.stream.events.Comment;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;
    public ResponseEntity<List<CommentDTO>> getCommentByPostId(int postId){
        List<CommentDTO> comments = commentMapper.getCommentByPostId(postId);
        return ResponseEntity.status(200).body(comments);
    }


    public ResponseEntity<String> insertComment(CommentDTO comment){
        commentMapper.insertComment(comment);
        return ResponseEntity.status(201).body("성공적으로 댓글이 추가 되었습니다.");
    }

    public ResponseEntity<String> updateComment(CommentDTO comment){
        commentMapper.updateComment(comment);
        return ResponseEntity.status(201).body("성공적으로 댓글이 수정되었습니다.");
    }

    public ResponseEntity<String> deleteComment(int commentId){
        commentMapper.deleteComment(commentId);
        return ResponseEntity.status(200).body("성공적으로 댓글이 삭제되었습니다.");
    }
}
