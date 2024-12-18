package com.example.schedulerv2.controller;

import com.example.schedulerv2.dto.commentDto.CommentRequest;
import com.example.schedulerv2.dto.commentDto.CommentResponseDto;
import com.example.schedulerv2.service.commentService.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> addComment(@Valid @RequestBody CommentRequest dto) {
        return new ResponseEntity<>(commentService.addComment(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDto> findCommentById(@PathVariable Long id){
        return new ResponseEntity<>(commentService.findCommentById(id),HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> findAllComment(){
        return new ResponseEntity<>(commentService.findAllComment(),HttpStatus.OK);
    }


}

