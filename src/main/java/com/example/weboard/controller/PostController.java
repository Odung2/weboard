package com.example.weboard.controller;

import com.example.weboard.dto.PostDTO;
import com.example.weboard.service.AuthService;
import com.example.weboard.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("weboard/posts")
public class PostController {

    private final PostService postService;
    private final AuthService authService;
    public PostController(PostService postService,AuthService authService) {
        this.postService = postService;
        this.authService = authService;
    }

    @GetMapping("/{postId}")
    public PostDTO getPostById(@PathVariable int postId) {
        return postService.getPostById(postId);
    }

    @GetMapping
    public List<PostDTO> getPostAll(){
        return postService.getPostAll();
    }

    @PostMapping
    public void insertPost(@RequestBody PostDTO post, @RequestHeader("Authorization") String jwttoken) {
        Integer userId = authService.getIdFromToken(jwttoken);
        post.setCreatedBy(userId);
        postService.insertPost(post);
    }

    @PutMapping("/{postId}")
    public void updatePost(@PathVariable int postId, @RequestBody PostDTO post, @RequestHeader("Authorization") String jwttoken) {
        post.setPostId(postId);
        Integer userId = authService.getIdFromToken(jwttoken);
        post.setUpdatedBy(userId);
        postService.updatePost(post);
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable int postId) {
        //삭제 시 createdBy와 jwttoken의 Id가 동일한지 검증하는 로직 필요
        postService.deletePost(postId);
    }
}
