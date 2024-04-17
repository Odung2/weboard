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

    public List<PostDTO> getPostAllByOffset(int offset) {
//        ApiResponse apiResponse = new ApiResponse(0, "성공적으로 게시물을 가져왔습니다.", postMapper.getPostAllByOffset(offset));
        return postMapper.getPostAllByOffset(offset);
    }

    /**
     * 아이디로 게시글 상세 조회
     *
     * @param postId
     * @return
     */
    public PostViewBO getPostById(int postId){ //밑에
        //List<CommentDTO> 리스트, 오브젝트 두개를 담는 그릇.
        PostViewBO postView = new PostViewBO();

        postView.setPost(postMapper.getPostById(postId));
        postView.setComment(commentService.getCommentByPostId(postId));
        return postView;
    }

    public PostDTO insertPost(PostDTO post, String jwttoken){
        Integer userId = authService.getIdFromToken(jwttoken);
        post.setCreatedBy(userId);
        postMapper.insert(post);
        return post;
    }

    public PostDTO updatePost(PostDTO post, int postId, String jwttoken){
        post.setPostId(postId);
        Integer userId = authService.getIdFromToken(jwttoken);
        post.setUpdatedBy(userId);
        postMapper.update(post);
        return post;
    }

    public int deletePost(int postId, String jwttoken) throws BadRequestException {
        int createdById = postMapper.getPostById(postId).getCreatedBy();
        int idFromJwt = authService.getIdFromToken(jwttoken);
        if(idFromJwt!=createdById){
            throw new BadRequestException("타인의 댓글은 삭제할 수 없습니다.");
        }
        return postMapper.delete(postId);
    }

}
