package com.example.weboard.service;

import com.example.weboard.mapper.PostMapper;
import com.example.weboard.model.Post;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private final PostMapper postMapper;

    public PostService(PostMapper postMapper){
        this.postMapper=postMapper;
    }

    public Post getPostById(int postId){
        return postMapper.getPostById(postId);
    }

    public void insertPost(Post post){
        postMapper.insertPost(post);
    }

    public void updatePost(Post post){
        postMapper.updatePost(post);
    }

    public void deletePost(int postId){
        postMapper.deletePost(postId);
    }

}
