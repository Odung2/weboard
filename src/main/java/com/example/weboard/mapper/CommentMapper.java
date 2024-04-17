package com.example.weboard.mapper;

import com.example.weboard.dto.CommentDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper{
    List<CommentDTO> getCommentByPostId(int postId);

    @Override
    void insert(CommentDTO comment);
    @Override
    void update(CommentDTO comment);
    @Override
    void delete(int commentId);
}
