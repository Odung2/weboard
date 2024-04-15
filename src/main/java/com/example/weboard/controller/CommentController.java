package com.example.weboard.controller;

import com.example.weboard.dto.ApiResponse;
import com.example.weboard.dto.CommentDTO;
import com.example.weboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("weboard/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse> getCommentByPostId(@PathVariable int postId){
        return commentService.getCommentByPostId(postId);
    }

    @PostMapping("/{postId}")
    public ResponseEntity<ApiResponse> insertComment(@RequestHeader("Authorization") String jwttoken,@PathVariable int postId, @RequestBody CommentDTO commentDTO){
        return commentService.insertComment(commentDTO, postId, jwttoken);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse> updateComment(@RequestHeader("Authorization") String jwttoken, @PathVariable int commentId, @RequestBody CommentDTO commentDTO){
        return commentService.updateComment(commentDTO, commentId);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(@RequestHeader("Authorization") String jwttoken,  @PathVariable int commentId){
        return commentService.deleteComment(commentId);
    }
}
