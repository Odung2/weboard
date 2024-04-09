package com.example.weboard.service;

import com.example.weboard.dto.ApiResponse;
import com.example.weboard.mapper.PostMapper;
import com.example.weboard.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class PostService {
    private final PostMapper postMapper;
    private final AuthService authService;
    public List<PostDTO> getPostAll() { return postMapper.getPostAll(); };

    public ResponseEntity<ApiResponse> getPostAllByOffset(int offset) {
        ApiResponse apiResponse = new ApiResponse(0, "성공적으로 게시물을 가져왔습니다.", postMapper.getPostAllByOffset(offset));
        return ResponseEntity.status(200).body(apiResponse);
    };
    public ResponseEntity<ApiResponse> getPostById(int postId){
        ApiResponse apiResponse = new ApiResponse(0, "성공적으로 게시물을 가져왔습니다.", postMapper.getPostById(postId));
        return ResponseEntity.status(200).body(apiResponse);
    }

    public ResponseEntity<ApiResponse> insertPost(PostDTO post, String jwttoken){
        Integer userId = authService.getIdFromToken(jwttoken);
        post.setCreatedBy(userId);
        postMapper.insertPost(post);
        ApiResponse apiResponse = new ApiResponse(0, "성공적으로 게시물을 작성했습니다.", null);
        return ResponseEntity.status(201).body(apiResponse);
    }

    public ResponseEntity<ApiResponse> updatePost(PostDTO post, int postId, String jwttoken){
        post.setPostId(postId);
        Integer userId = authService.getIdFromToken(jwttoken);
        post.setUpdatedBy(userId);
        postMapper.updatePost(post);
        ApiResponse apiResponse = new ApiResponse(0, "성공적으로 게시물을 수정했습니다.", null);
        return ResponseEntity.status(200).body(apiResponse);
    }

    public ResponseEntity<ApiResponse> deletePost(int postId, String jwttoken) throws BadRequestException {
        int createdById = postMapper.getPostById(postId).getCreatedBy();
        int idFromJwt = authService.getIdFromToken(jwttoken);
        if(idFromJwt!=createdById){
            throw new BadRequestException("타인의 댓글은 삭제할 수 없습니다.");
        }
        postMapper.deletePost(postId);
        ApiResponse apiResponse = new ApiResponse(0, "성공적으로 게시물을 삭제했습니다.", null);
        return ResponseEntity.status(200).body(apiResponse);
    }

}
