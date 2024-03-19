package com.example.weboard.controller;

import com.example.weboard.model.Comment;
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
    public ResponseEntity<Comment> getCommentByPostId(@PathVariable int postId){
        Comment comment = commentService.getCommentByPostId(postId);
        if(comment != null){
            return ResponseEntity.ok(comment);
        } else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/insertComment")
    public ResponseEntity<Void> insertComment(@RequestBody Comment comment){
        commentService.insertComment(comment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable int commentId, @RequestBody Comment comment){
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
