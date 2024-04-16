package com.example.weboard.controller;

import com.example.weboard.dto.ApiResponse;
import com.example.weboard.dto.PostDTO;
import com.example.weboard.service.AuthService;
import com.example.weboard.service.PostService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("weboard/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final AuthService authService;

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse> getPostById(@PathVariable int postId) {
        return postService.getPostById(postId);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getPostAllByOffset(@RequestParam int offset) {
        return postService.getPostAllByOffset(offset);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> insertPost(@RequestBody PostDTO postDTO, @RequestHeader("Authorization") String jwttoken) {
        return postService.insertPost(postDTO, jwttoken);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse> updatePost(@RequestHeader("Authorization") String jwttoken, @PathVariable int postId, @RequestBody PostDTO postDTO) {
        return postService.updatePost(postDTO, postId, jwttoken);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse> deletePost(@RequestHeader(   "Authorization") String jwttoken, @PathVariable int postId) throws BadRequestException {
        return postService.deletePost(postId, jwttoken);
    }
}
