package com.example.weboard.controller;

import com.example.weboard.dto.ApiResponse;
import com.example.weboard.dto.PostViewBO;
import com.example.weboard.exception.UnauthorizedAccessException;
import com.example.weboard.param.BasePagingParam;
import com.example.weboard.param.InsertPostParam;
import com.example.weboard.param.UpdatePostParam;
import com.example.weboard.response.PublicPostRes;
import com.example.weboard.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("weboard/posts")
@RequiredArgsConstructor
public class PostController extends BaseController{

    private final PostService postService;

    /**
     * 모든 게시물을 오프셋 기준으로 n개씩 반환합니다.
     *
     * @param basePagingParam 오프셋 값
     * @return 게시물 목록과 상태 메시지를 담은 ResponseEntity
     */
    @Operation(summary = "모든 게시물을 오프셋 기준으로 n개씩 반환합니다.")
    @PostMapping("/public")
    public ResponseEntity<ApiResponse<List<PublicPostRes>>> getPostAllByOffset(
            @RequestBody @Valid BasePagingParam basePagingParam) {
        return ok(postService.getPublicPostIntroAllByOffset(basePagingParam));
    }

    /**
     * 특정 게시물을 ID로 조회합니다.
     *
     * @param postId 게시물 ID
     * @return 조회된 게시물과 상태 메시지를 담은 ResponseEntity
     */
    @Operation(summary = "특정 게시물을 ID로 조회합니다.")
    @GetMapping("/public/{postId}")
    public ResponseEntity<ApiResponse<PostViewBO>> getPostById(
            @PathVariable int postId) {
        return ok(postService.getPostViewById(postId));
    }

    /**
     * 새로운 게시물을 추가합니다.
     *
     * @param insertPostParam 추가할 게시물 데이터
     * @param jwttoken        인증 토큰
     * @return 추가된 게시물과 상태 메시지를 담은 ResponseEntity
     */
    @Operation(summary = "새로운 게시물을 추가합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<String>> insertPost(
            @RequestAttribute("reqId") int id,
            @RequestBody @Valid InsertPostParam insertPostParam) {
        return ok(postService.insertPost(insertPostParam, id));
    }

    /**
     * 유저가 작성한 본인의 게시물을 업데이트(수정)합니다.
     *
     * @param jwttoken        인증 토큰
     * @param postId          업데이트할 게시물의 ID
     * @param updatePostParam 업데이트할 게시물 데이터
     * @return 업데이트된 게시물과 상태 메시지를 담은 ResponseEntity
     */
    @Operation(summary = "유저가 작성한 본인의 게시물을 업데이트(수정)합니다.")
    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse<String>> updatePost(
            @RequestAttribute("reqId") int id,
            @PathVariable int postId,
            @RequestBody @Valid UpdatePostParam updatePostParam) throws UnauthorizedAccessException {
        return ok(postService.updatePost(updatePostParam, postId, id));
    }

    /**
     * 유저가 작성한 본인의 게시물을 삭제합니다.
     *
     * @param jwttoken 인증 토큰
     * @param postId 삭제할 게시물의 ID
     * @return 삭제된 게시물 ID와 상태 메시지를 담은 ResponseEntity
     */
    @Operation(summary = "유저가 작성한 본인의 게시물을 삭제합니다.")
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Integer>> deletePost(
            @RequestAttribute("reqId") int id,
            @PathVariable int postId) throws BadRequestException, UnauthorizedAccessException {
        return ok(postService.deletePost(postId, id));
    }

}
