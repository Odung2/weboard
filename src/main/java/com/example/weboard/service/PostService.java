package com.example.weboard.service;

import com.example.weboard.dto.PostViewBO;
import com.example.weboard.mapper.PostMapper;
import com.example.weboard.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class PostService {
    private final PostMapper postMapper;
    private final AuthService authService;
    private final CommentService commentService;
    public List<PostDTO> getPostAll() { return postMapper.getPostAll(); }

    /**
     * 지정된 오프셋부터 모든 게시물을 조회합니다.
     * @param offset 시작할 위치의 오프셋
     * @return 오프셋 기준으로 조회된 게시물 목록
     */
    public List<PostDTO> getPostAllByOffset(int offset) {
        return postMapper.getPostAllByOffset(offset);
    }

    /**
     * 주어진 아이디에 해당하는 게시글의 상세 정보를 조회합니다.
     * 게시글 정보와 해당 게시글의 모든 댓글을 포함한 PostViewBO 객체를 반환합니다.
     * @param postId 조회할 게시글의 ID
     * @return 게시글과 댓글 정보를 포함한 PostViewBO 객체
     */
    public PostViewBO getPostById(int postId){
        PostViewBO postView = new PostViewBO();
        postView.setPost(postMapper.getPostById(postId));
        postView.setComment(commentService.getCommentByPostId(postId));
        return postView;
    }

    /**
     * 새로운 게시물을 추가합니다.
     * JWT 토큰에서 사용자 ID를 추출하여 게시물의 생성자로 설정한 후 데이터베이스에 삽입합니다.
     * @param post 삽입할 게시물 데이터
     * @param jwttoken 요청자의 JWT 토큰
     * @return 데이터베이스에 삽입된 게시물 객체
     */
    public PostDTO insertPost(PostDTO post, String jwttoken){
        Integer userId = authService.getIdFromToken(jwttoken);
        post.setCreatedBy(userId);
        postMapper.insert(post);
        return post;
    }

    /**
     * 주어진 ID의 게시물을 업데이트합니다.
     * JWT 토큰에서 사용자 ID를 추출하여 게시물의 수정자로 설정한 후 데이터베이스에 업데이트합니다.
     * @param post 업데이트할 게시물 데이터
     * @param postId 업데이트할 게시물의 ID
     * @param jwttoken 요청자의 JWT 토큰
     * @return 데이터베이스에 업데이트된 게시물 객체
     */
    public PostDTO updatePost(PostDTO post, int postId, String jwttoken){
        post.setPostId(postId);
        Integer userId = authService.getIdFromToken(jwttoken);
        post.setUpdatedBy(userId);
        postMapper.update(post);
        return post;
    }

    /**
     * 주어진 ID의 게시물을 삭제합니다.
     * JWT 토큰에서 추출한 사용자 ID가 게시물의 생성자 ID와 일치해야 삭제가 가능합니다.
     * @param postId 삭제할 게시물의 ID
     * @param jwttoken 요청자의 JWT 토큰
     * @return 삭제된 게시물의 수 (성공적으로 삭제되면 1)
     * @throws BadRequestException 게시물의 생성자가 아닌 경우 예외를 던집니다.
     */
    public int deletePost(int postId, String jwttoken) throws BadRequestException {
        int createdById = postMapper.getPostById(postId).getCreatedBy();
        int idFromJwt = authService.getIdFromToken(jwttoken);
        if(idFromJwt!=createdById){
            throw new BadRequestException("타인의 게시글은 삭제할 수 없습니다.");
        }
        return postMapper.delete(postId);
    }


}
