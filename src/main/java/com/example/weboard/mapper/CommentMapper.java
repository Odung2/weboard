package com.example.weboard.mapper;

import com.example.weboard.dto.CommentDTO;
import com.example.weboard.response.CommentRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper{
    List<CommentDTO> getCommentByPostId(int postId);
    CommentDTO getCommentByCommentId(int commentId);
    @Override
    int insert(CommentDTO comment);
    @Override
    int update(CommentDTO comment);
    @Override
    int delete(int commentId);

    List<CommentRes> getComments(int postId);
}
