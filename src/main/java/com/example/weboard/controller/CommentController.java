package com.example.weboard.controller;

import com.example.weboard.dto.CommentDTO;
import com.example.weboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("weboard/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{postId}")
    public List<CommentDTO> getCommentByPostId(@PathVariable int postId){
        return commentService.getCommentByPostId(postId);
    }

    @PostMapping
    public void insertComment(@RequestBody CommentDTO commentDTO){
        commentService.insertComment(commentDTO);
    }

    @PutMapping("/{commentId}")
    public void updateComment(@PathVariable int commentId, @RequestBody CommentDTO commentDTO){
        commentDTO.setCommentId(commentId);
        commentService.updateComment(commentDTO);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable int commentId){
        commentService.deleteComment(commentId);
    }
}
