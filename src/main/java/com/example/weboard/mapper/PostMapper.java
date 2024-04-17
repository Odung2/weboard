package com.example.weboard.mapper;

import com.example.weboard.dto.PostDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper extends BaseMapper {

    PostDTO getPostById(int postId);

    List<PostDTO> getPostAll();
    List<PostDTO> getPostAllByOffset(int offset);
    @Override
    int insert(PostDTO post);
    @Override
    int update(PostDTO post);
    @Override
    int delete(int postId);
}
