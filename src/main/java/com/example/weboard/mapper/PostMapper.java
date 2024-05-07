package com.example.weboard.mapper;

import com.example.weboard.dto.PostDTO;
import com.example.weboard.param.BasePagingParam;
import com.example.weboard.response.DetailPostRes;
import com.example.weboard.response.PublicPostRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper extends BaseMapper {

    PostDTO getPostById(int postId);

    List<PostDTO> getPostAll();
    List<PostDTO> getPostAllByOffset(@Param("offset") int currpage, @Param("pagesize") int pagesize);
    @Override
    int insert(PostDTO post);
    @Override
    int update(PostDTO post);
    @Override
    int delete(int postId);

    DetailPostRes getDetailPostRes(int postId);

    List<PublicPostRes> getPublicPostIntroAllByOffset(int currPage, int pageSize);
}
