package com.example.weboard.service;

import com.example.weboard.dto.ApiResponse;
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
    private final AuthService authService;
    public ResponseEntity<ApiResponse> getCommentByPostId(int postId){
        List<CommentDTO> comments = commentMapper.getCommentByPostId(postId);
        ApiResponse apiResponse = new ApiResponse<>(0, "성공적으로 댓글을 가져왔습니다.", comments);
        return ResponseEntity.status(200).body(apiResponse);
    }


    public ResponseEntity<ApiResponse> insertComment(CommentDTO comment, int postId, String jwttoken){
        int id = authService.getIdFromToken(jwttoken);

        comment.setPostId(postId);
        comment.setUserId(id);
        commentMapper.insert(comment);
        ApiResponse apiResponse = new ApiResponse<>(0, "성공적으로 댓글이 추가 되었습니다.", null);
        return ResponseEntity.status(201).body(apiResponse);
    }

    public ResponseEntity<ApiResponse> updateComment(CommentDTO comment, int commentId){
        comment.setCommentId(commentId);
        commentMapper.update(comment);
        ApiResponse apiResponse = new ApiResponse<>(0, "성공적으로 댓글이 수정되었습니다.", null);
        return ResponseEntity.status(201).body(apiResponse);
    }

    public ResponseEntity<ApiResponse> deleteComment(int commentId){
        commentMapper.delete(commentId);
        ApiResponse apiResponse = new ApiResponse<>(0, "성공적으로 댓글이 삭제되었습니다.", null);
        return ResponseEntity.status(200).body(apiResponse);
    }
}
