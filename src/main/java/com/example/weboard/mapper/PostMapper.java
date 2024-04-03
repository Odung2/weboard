package com.example.weboard.mapper;

import com.example.weboard.dto.PostDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {

    PostDTO getPostById(int postId);

    List<PostDTO> getPostAll();
    List<PostDTO> getPostAllByOffset(int offset);
    void insertPost(PostDTO post);

    void updatePost(PostDTO post);

    void deletePost(int postId);
}
