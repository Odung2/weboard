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

    /**
     * 모든 게시물을 오프셋 기준으로 n개씩 반환합니다.
     *
     * @param offset
     * @return 게시물 목록과 상태 메시지를 담은 ResponseEntity
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<PostDTO>>> getPostAllByOffset(
            @RequestParam int offset) {
        return ok(FrkConstants.getAllPost, postService.getPostAllByOffset(offset));
    }

    /**
     * 특정 게시물을 ID로 조회합니다.
     *
     * @param postId 게시물 ID
     * @return 조회된 게시물과 상태 메시지를 담은 ResponseEntity
     */
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostViewBO>> getPostById(
            @PathVariable int postId) {
        return ok(FrkConstants.getPostView, postService.getPostById(postId));
    }

    /**
     * 새로운 게시물을 추가합니다.
     * Authorization 헤더를 통해 요청 인증을 수행하고 이후 작업을 진행합니다.
     *
     * @param postDTO 추가할 게시물 데이터
     * @param jwttoken 인증 토큰
     * @return 추가된 게시물과 상태 메시지를 담은 ResponseEntity
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PostDTO>> insertPost(
            @RequestBody PostDTO postDTO, @RequestHeader("Authorization") String jwttoken) {
        return ok(FrkConstants.insertPost, postService.insertPost(postDTO, jwttoken));
    }

    /**
     * 유저가 작성한 본인의 게시물을 업데이트(수정)합니다.
     * Authorization 헤더를 통해 요청 인증을 수행하고 이후 작업을 진행합니다.
     *
     * @param jwttoken 인증 토큰
     * @param postId 업데이트할 게시물의 ID
     * @param postDTO 업데이트할 게시물 데이터
     * @return 업데이트된 게시물과 상태 메시지를 담은 ResponseEntity
     */
    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDTO>> updatePost(
            @RequestHeader("Authorization") String jwttoken, @PathVariable int postId, @RequestBody PostDTO postDTO) {
        return ok(FrkConstants.updatePost, postService.updatePost(postDTO, postId, jwttoken));
    }

    /**
     * 유저가 작성한 본인의 게시물을 삭제합니다.
     * Authorization 헤더를 통해 요청 인증을 수행하고 이후 작업을 진행합니다.
     *
     * @param jwttoken 인증 토큰
     * @param postId 삭제할 게시물의 ID
     * @return 삭제된 게시물 ID와 상태 메시지를 담은 ResponseEntity
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Integer>> deletePost(
            @RequestHeader("Authorization") String jwttoken, @PathVariable int postId) throws BadRequestException {
        return ok(FrkConstants.deletePost, postService.deletePost(postId, jwttoken));
    }

}
