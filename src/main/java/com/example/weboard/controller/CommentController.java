package com.example.weboard.controller;

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
    public ResponseEntity<List<CommentDTO>> getCommentByPostId(@PathVariable int postId){
        return commentService.getCommentByPostId(postId);
    }

    @PostMapping
    public ResponseEntity<String> insertComment(@RequestHeader("Authroization") String jwttoken, @RequestBody CommentDTO commentDTO){
        return commentService.insertComment(commentDTO);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<String> updateComment(@RequestHeader("Authroization") String jwttoken, @PathVariable int commentId, @RequestBody CommentDTO commentDTO){
        commentDTO.setCommentId(commentId);
        return commentService.updateComment(commentDTO);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@RequestHeader("Authroization") String jwttoken,  @PathVariable int commentId){
        return commentService.deleteComment(commentId);
    }
}
