package com.example.weboard.mapper;

import com.example.weboard.dto.CommentDTO;
import com.example.weboard.dto.PostDTO;
import com.example.weboard.dto.UserDTO;

public interface BaseMapper {

    void insert(UserDTO user);
    void insert(PostDTO post);
    void insert(CommentDTO comment);

    void update(UserDTO user);
    void update(PostDTO post);
    void update(CommentDTO comment);

    void delete(int DTOId);

}
