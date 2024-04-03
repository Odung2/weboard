package com.example.weboard.controller;

import com.example.weboard.dto.PostDTO;
import com.example.weboard.service.AuthService;
import com.example.weboard.service.PostService;
import lombok.RequiredArgsConstructor;
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
    public PostDTO getPostById(@PathVariable int postId) {
        return postService.getPostById(postId);
    }

//    @GetMapping
//    public List<PostDTO> getPostAll(){
//        return postService.getPostAll();
//    }
    @GetMapping()
    public List<PostDTO> getPostAllByOffset(@RequestParam int offset) {
        return postService.getPostAllByOffset(offset);
    }

    @PostMapping
    public void insertPost(@RequestBody PostDTO postDTO, @RequestHeader("Authorization") String jwttoken) {
        Integer userId = authService.getIdFromToken(jwttoken);
        postDTO.setCreatedBy(userId);
        postService.insertPost(postDTO);
    }

    @PutMapping("/{postId}")
    public void updatePost(@PathVariable int postId, @RequestBody PostDTO postDTO, @RequestHeader("Authorization") String jwttoken) {
        postDTO.setPostId(postId);
        Integer userId = authService.getIdFromToken(jwttoken);
        postDTO.setUpdatedBy(userId);
        postService.updatePost(postDTO);
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@RequestHeader("Authorization") String jwttoken, @PathVariable int postId) {
        //삭제 시 createdBy와 jwttoken의 Id가 동일한지 검증하는 로직 필요
        postService.deletePost(postId);
    }
}
