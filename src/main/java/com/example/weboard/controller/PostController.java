package com.example.weboard.controller;

import com.example.weboard.dto.ApiResponse;
import com.example.weboard.dto.FrkConstants;
import com.example.weboard.dto.PostDTO;
import com.example.weboard.dto.PostViewBO;
import com.example.weboard.service.PostService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("weboard/posts")
@RequiredArgsConstructor
public class PostController extends BaseController{

    private final PostService postService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostDTO>>> getPostAllByOffset(
            @RequestParam int offset) {
        return ok(FrkConstants.getAllPost, postService.getPostAllByOffset(offset));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostViewBO>> getPostById(
            @PathVariable int postId) {
        return ok(FrkConstants.getPostView, postService.getPostById(postId));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PostDTO>> insertPost(
            @RequestBody PostDTO postDTO, @RequestHeader("Authorization") String jwttoken) {
        return ok(FrkConstants.insertPost, postService.insertPost(postDTO, jwttoken));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDTO>> updatePost(
            @RequestHeader("Authorization") String jwttoken, @PathVariable int postId, @RequestBody PostDTO postDTO) {
        return ok(FrkConstants.updatePost, postService.updatePost(postDTO, postId, jwttoken));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Integer>> deletePost(
            @RequestHeader("Authorization") String jwttoken, @PathVariable int postId) throws BadRequestException {
        return ok(FrkConstants.deletePost, postService.deletePost(postId, jwttoken));
    }
}
