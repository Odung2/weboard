package com.example.weboard.controller;

import com.example.weboard.dto.ApiResponse;
import com.example.weboard.dto.CommentDTO;
import com.example.weboard.dto.FrkConstants;
import com.example.weboard.param.InsertCommentParam;
import com.example.weboard.param.UpdateCommentParam;
import com.example.weboard.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("weboard/comments")
@RequiredArgsConstructor
public class CommentController extends BaseController{

    private final CommentService commentService;

    /**
     * 특정 게시물의 댓글을 조회합니다.
     *
     * @param postId 댓글을 검색할 게시물의 ID
     * @return CommentDTO 리스트와 상태 메시지를 포함하는 ResponseEntity를 반환합니다.
     */
    @Operation(summary = "특정 게시물의 댓글을 조회합니다.")
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<List<CommentDTO>>> getCommentByPostId(
            @PathVariable int postId){
        return ok(FrkConstants.getComments, commentService.getCommentByPostId(postId));
    }

    /**
     * 특정 게시물에 새 댓글을 추가합니다.
     *
     * @param jwttoken 요청을 검증하기 위한 인증 토큰
     * @param postId 댓글이 추가될 게시물의 ID
     * @param insertCommentParam 댓글의 상세 정보를 담고 있는 CommentDTO 객체
     * @return 추가된 CommentDTO와 상태 메시지를 포함하는 ResponseEntity를 반환합니다.
     */
    @Operation(summary = "특정 게시물에 새 댓글을 추가합니다.")
    @PostMapping("/{postId}")
    public ResponseEntity<ApiResponse<CommentDTO>> insertComment(
            @RequestHeader("Authorization") String jwttoken,
            @RequestHeader(value="Refresh-token", defaultValue = "") String refreshToken,
            @PathVariable int postId, @RequestBody @Valid InsertCommentParam insertCommentParam){
        return ok(FrkConstants.insertComment, commentService.insertComment(insertCommentParam, postId, jwttoken));
    }

    /**
     * 기존 댓글을 업데이트합니다.
     *
     * @param jwttoken 요청을 검증하기 위한 인증 토큰
     * @param commentId 업데이트될 댓글의 ID
     * @param updateCommentParam 업데이트할 내용을 담고 있는 CommentDTO 객체
     * @return 업데이트된 CommentDTO와 상태 메시지를 포함하는 ResponseEntity를 반환합니다.
     */
    @Operation(summary = "기존 댓글을 업데이트합니다.")
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentDTO>> updateComment(
            @RequestHeader("Authorization") String jwttoken,
            @RequestHeader(value="Refresh-token", defaultValue = "") String refreshToken,
            @PathVariable int commentId, @RequestBody @Valid UpdateCommentParam updateCommentParam){
        return ok(FrkConstants.updateComment, commentService.updateComment(updateCommentParam, commentId));
    }

    /**
     * 기존 댓글을 삭제합니다.
     *
     * @param jwttoken 요청을 검증하기 위한 인증 토큰
     * @param commentId 삭제될 댓글의 ID
     * @return 삭제된 댓글의 ID와 상태 메시지를 포함하는 ResponseEntity를 반환합니다.
     */
    @Operation(summary = "기존 댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Integer>> deleteComment(
            @RequestHeader("Authorization") String jwttoken,
            @RequestHeader(value="Refresh-token", defaultValue = "") String refreshToken,
            @PathVariable int commentId){
        return ok(FrkConstants.deleteComment, commentService.deleteComment(commentId));
    }

}
