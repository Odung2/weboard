package com.example.weboard.service;

import com.example.weboard.mapper.CommentMapper;
import com.example.weboard.dto.CommentDTO;
import com.example.weboard.param.InsertCommentParam;
import com.example.weboard.param.UpdateCommentParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;
    private final AuthService authService;
    /**
     * 특정 게시물의 모든 댓글을 조회합니다.
     * @param postId 댓글을 조회할 게시물의 ID
     * @return 조회된 댓글 목록
     */
    public List<CommentDTO> getCommentByPostId(int postId){
        return commentMapper.getCommentByPostId(postId);
    }

    /**
     * 새로운 댓글을 추가합니다.
     * JWT 토큰에서 사용자 ID를 추출하고, 댓글에 게시물 ID와 사용자 ID를 설정한 후 데이터베이스에 삽입합니다.
     * @param insertCommentParam 삽입할 댓글 데이터
     * @param postId 댓글이 속할 게시물의 ID
     * @param jwttoken 요청자의 JWT 토큰
     * @return 데이터베이스에 삽입된 댓글 객체
     */
    public CommentDTO insertComment(InsertCommentParam insertCommentParam, int postId, String jwttoken){
        int id = authService.getIdFromToken(jwttoken);
        CommentDTO comment = new CommentDTO();

        comment.setCommentText(insertCommentParam.getCommentText());

        comment.setPostId(postId);
        comment.setUserId(id);
        commentMapper.insert(comment);
        return comment;
    }

    /**
     * 기존의 댓글을 업데이트합니다.
     * 댓글 ID를 설정하고, 수정된 내용을 데이터베이스에 업데이트합니다.
     * @param updateCommentParam 업데이트할 댓글 데이터
     * @param commentId 업데이트될 댓글의 ID
     * @return 데이터베이스에 업데이트된 댓글 객체
     */
    public CommentDTO updateComment(UpdateCommentParam updateCommentParam, int commentId){
        CommentDTO comment = new CommentDTO();

        comment.setCommentText(updateCommentParam.getCommentText());
        comment.setUserId(updateCommentParam.getUserId());
        comment.setPostId(updateCommentParam.getPostId());

        comment.setCommentId(commentId);
        commentMapper.update(comment);
        return comment;
    }

    /**
     * 특정 댓글을 삭제합니다.
     * 댓글 ID를 사용하여 해당 댓글을 데이터베이스에서 삭제합니다.
     * @param commentId 삭제할 댓글의 ID
     * @return 삭제 결과 (성공 시 1, 실패 시 0)
     */
    public int deleteComment(int commentId){
        return commentMapper.delete(commentId);
    }

}
