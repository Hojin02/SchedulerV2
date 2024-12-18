package com.example.schedulerv2.service.commentRepository;

import com.example.schedulerv2.entity.Comment;
import com.example.schedulerv2.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

@Repository
public interface CommentReopsitory extends JpaRepository<Comment,Long> {

    default Comment findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no Comment for this id."));
    }
}
