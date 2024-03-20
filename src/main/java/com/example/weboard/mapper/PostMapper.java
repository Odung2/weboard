package com.example.weboard.mapper;

import com.example.weboard.model.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {

    Post getPostById(int postId);

    List<Post> getPostAll();
    void insertPost(Post post);

    void updatePost(Post post);

    void deletePost(int postId);
}
