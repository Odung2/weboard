package com.example.weboard.controller;

import com.example.weboard.dto.CommentDTO;
import com.example.weboard.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("weboard/comments")
@Controller
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService){
        this.commentService=commentService;
    }

    @GetMapping("/{postId}")
    public ResponseEntity<CommentDTO> getCommentByPostId(@PathVariable int postId){
        CommentDTO comment = commentService.getCommentByPostId(postId);
        if(comment != null){
            return ResponseEntity.ok(comment);
        } else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> insertComment(@RequestBody CommentDTO comment){
        commentService.insertComment(comment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable int commentId, @RequestBody CommentDTO comment){
        comment.setCommentId(commentId);
        commentService.updateComment(comment);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable int commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

}
