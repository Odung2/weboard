package com.example.weboard.controller;

import com.example.weboard.dto.CommentDTO;
import com.example.weboard.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("weboard/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService){
        this.commentService=commentService;
    }

    @GetMapping("/{postId}")
    public CommentDTO getCommentByPostId(@PathVariable int postId){
        return commentService.getCommentByPostId(postId);
    }

    @PostMapping
    public void insertComment(@RequestBody CommentDTO comment){
        commentService.insertComment(comment);
    }

    @PutMapping("/{commentId}")
    public void updateComment(@PathVariable int commentId, @RequestBody CommentDTO comment){
        comment.setCommentId(commentId);
        commentService.updateComment(comment);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable int commentId){
        commentService.deleteComment(commentId);
    }
}
