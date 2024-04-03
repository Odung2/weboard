package com.example.weboard.service;

import com.example.weboard.mapper.PostMapper;
import com.example.weboard.dto.PostDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostMapper postMapper;

    public PostService(PostMapper postMapper){
        this.postMapper=postMapper;
    }

    public List<PostDTO> getPostAll() { return postMapper.getPostAll(); };

    public List<PostDTO> getPostAllByOffset(Integer offset) { return postMapper.getPostAllByOffset(offset); };
    public PostDTO getPostById(int postId){
        return postMapper.getPostById(postId);
    }

    public void insertPost(PostDTO post){
        postMapper.insertPost(post);
    }

    public void updatePost(PostDTO post){
        postMapper.updatePost(post);
    }

    public void deletePost(int postId){
        postMapper.deletePost(postId);
    }

}
