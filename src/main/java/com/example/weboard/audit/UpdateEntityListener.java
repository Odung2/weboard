package com.example.weboard.audit;

import com.example.weboard.dto.CommentDTO;
import com.example.weboard.dto.PostDTO;
import com.example.weboard.dto.UserDTO;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.WebRequest;

public class UpdateEntityListener {
    @PrePersist
    @PreUpdate
    public void beforeUpdate(UserDTO param){
        int id = (int) RequestContextHolder.getRequestAttributes().getAttribute("reqId",  WebRequest.SCOPE_REQUEST);
        param.setUpdatedBy(id);
    }

}
