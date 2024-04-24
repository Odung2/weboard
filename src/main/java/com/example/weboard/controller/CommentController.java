package com.example.weboard.controller;

import com.example.weboard.dto.ApiResponse;
import com.example.weboard.dto.CommentDTO;
import com.example.weboard.dto.FrkConstants;
import com.example.weboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("weboard/comments")
@RequiredArgsConstructor
public class CommentController extends BaseController{

    private final CommentService commentService;

    /**
     * 특정 게시물의 댓글을 조회합니다.
     * 이 메서드는 특정 게시물에 연결된 CommentDTO 객체 목록을 검색합니다.
     *
     * @param postId 댓글을 검색할 게시물의 ID
     * @return CommentDTO 리스트와 상태 메시지를 포함하는 ResponseEntity를 반환합니다.
     */
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<List<CommentDTO>>> getCommentByPostId(
            @PathVariable int postId){
        return ok(FrkConstants.getComments, commentService.getCommentByPostId(postId));
    }

    /**
     * 특정 게시물에 새 댓글을 추가합니다.
     * 이 메서드는 제공된 CommentDTO 객체를 사용하여 게시물에 새 댓글을 추가합니다.
     * Authorization 헤더를 통해 요청 인증을 수행하고 이후 작업을 진행합니다.
     *
     * @param jwttoken 요청을 검증하기 위해 필요한 인증 토큰
     * @param postId 댓글이 추가될 게시물의 ID
     * @param commentDTO 댓글의 상세 정보를 담고 있는 CommentDTO 객체
     * @return 추가된 CommentDTO와 상태 메시지를 포함하는 ResponseEntity를 반환합니다.
     */
    @PostMapping("/{postId}")
    public ResponseEntity<ApiResponse<CommentDTO>> insertComment(
            @RequestHeader("Authorization") String jwttoken, @PathVariable int postId, @RequestBody CommentDTO commentDTO){
        return ok(FrkConstants.insertComment, commentService.insertComment(commentDTO, postId, jwttoken));
    }

    /**
     * 기존 댓글을 업데이트합니다.
     * 이 메서드는 commentId로 식별되는 기존 댓글의 상세 정보를 업데이트합니다.
     * Authorization 헤더를 통해 요청 인증을 수행하고 이후 작업을 진행합니다.
     *
     * @param jwttoken 요청을 검증하기 위해 필요한 인증 토큰
     * @param commentId 업데이트될 댓글의 ID
     * @param commentDTO 업데이트할 내용을 담고 있는 CommentDTO 객체
     * @return 업데이트된 CommentDTO와 상태 메시지를 포함하는 ResponseEntity를 반환합니다.
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentDTO>> updateComment(
            @RequestHeader("Authorization") String jwttoken, @PathVariable int commentId, @RequestBody CommentDTO commentDTO){
        return ok(FrkConstants.updateComment, commentService.updateComment(commentDTO, commentId));
    }

    /**
     * 기존 댓글을 삭제합니다.
     * 이 메서드는 commentId로 식별되는 댓글을 삭제합니다.
     * Authorization 헤더를 통해 요청 인증을 수행하고 이후 작업을 진행합니다.
     *
     * @param jwttoken 요청을 검증하기 위해 필요한 인증 토큰
     * @param commentId 삭제될 댓글의 ID
     * @return 삭제된 댓글의 ID와 상태 메시지를 포함하는 ResponseEntity를 반환합니다.
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Integer>> deleteComment(
            @RequestHeader("Authorization") String jwttoken,  @PathVariable int commentId){
        return ok(FrkConstants.deleteComment, commentService.deleteComment(commentId));
    }

}
