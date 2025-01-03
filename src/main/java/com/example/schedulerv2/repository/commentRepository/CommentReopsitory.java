package com.example.schedulerv2.repository.commentRepository;

import com.example.schedulerv2.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentReopsitory extends JpaRepository<Comment,Long> {

}
