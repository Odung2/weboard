package com.example.weboard.controller;

import com.example.weboard.dto.ApiResponse;
import com.example.weboard.dto.CommentDTO;
import com.example.weboard.dto.FrkConstants;
import com.example.weboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("weboard/comments")
@RequiredArgsConstructor
public class CommentController extends BaseController{

    private final CommentService commentService;

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<List<CommentDTO>>> getCommentByPostId(
            @PathVariable int postId){
        return ok(FrkConstants.getComments ,commentService.getCommentByPostId(postId));
    }

    @PostMapping("/{postId}")
    public ResponseEntity<ApiResponse<CommentDTO>> insertComment(
            @RequestHeader("Authorization") String jwttoken,@PathVariable int postId, @RequestBody CommentDTO commentDTO){
        return ok(FrkConstants.insertComment, commentService.insertComment(commentDTO, postId, jwttoken));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentDTO>> updateComment(
            @RequestHeader("Authorization") String jwttoken, @PathVariable int commentId, @RequestBody CommentDTO commentDTO){
        return ok(FrkConstants.updateComment, commentService.updateComment(commentDTO, commentId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Integer>> deleteComment(
            @RequestHeader("Authorization") String jwttoken,  @PathVariable int commentId){
        return ok(FrkConstants.deleteComment, commentService.deleteComment(commentId));
    }
}
