package com.example.weboard.audit;

import com.example.weboard.dto.CommentDTO;
import com.example.weboard.dto.PostDTO;
import com.example.weboard.dto.UserDTO;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.context.request.WebRequest;

public class UpdateEntityListener {
//
//    @PrePersist
//    @PreUpdate
//    public void beforeUpdate(UserDTO param){
////        WebRequest request = WebRequest
//        int id = (int) request.getAttribute("reqId", WebRequest.SCOPE_REQUEST);
//        param.setUpdatedBy(id);
//    }
//
//    @PrePersist
//    @PreUpdate
//    public void beforeUpdate(
//            @RequestAttribute("reqId") int id, PostDTO param){
//        param.setUpdatedBy(id);
//    }
//
//    @PrePersist
//    @PreUpdate
//    public void beforeUpdate(
//            @RequestAttribute("reqId") int id, CommentDTO param){
//        param.setUpdatedBy(id);
//    }
}
