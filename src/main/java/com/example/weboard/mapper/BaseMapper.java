package com.example.weboard.mapper;

import com.example.weboard.dto.CommentDTO;
import com.example.weboard.dto.PostDTO;
import com.example.weboard.dto.UserDTO;

public interface BaseMapper {

    int insert(UserDTO user);
    int insert(PostDTO post);
    int insert(CommentDTO comment);

    int update(UserDTO user);
    int update(PostDTO post);
    int update(CommentDTO comment);

    int delete(int DTOId);

}
