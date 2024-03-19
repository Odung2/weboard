package com.example.weboard.mapper;

import com.example.weboard.model.Post;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper {

    Post getPostById(int postId);

    void insertPost(Post post);

    void updatePost(Post post);

    void deletePost(int postId);
}
