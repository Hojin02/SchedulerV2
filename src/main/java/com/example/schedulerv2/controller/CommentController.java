package com.example.schedulerv2.controller;

import com.example.schedulerv2.dto.commentDto.CommentRequest;
import com.example.schedulerv2.dto.commentDto.CommentResponseDto;
import com.example.schedulerv2.dto.commentDto.CommentUpdateRequestDto;
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

    @PostMapping// 댓글 작성 메소드
    public ResponseEntity<CommentResponseDto> addComment(@Valid @RequestBody CommentRequest dto) {
        return new ResponseEntity<>(commentService.addComment(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")// 댓글 단건 조회 메소드
    public ResponseEntity<CommentResponseDto> findCommentById(@PathVariable Long id){
        return new ResponseEntity<>(commentService.findCommentById(id),HttpStatus.OK);
    }
    @GetMapping// 전체 댓글 조회 메소드
    public ResponseEntity<List<CommentResponseDto>> findAllComment(){
        return new ResponseEntity<>(commentService.findAllComment(),HttpStatus.OK);
    }

    @PatchMapping("/{id}") // 댓글 수정 메소드
    public ResponseEntity<CommentResponseDto> modifyCommentById(@PathVariable Long id, @Valid @RequestBody CommentUpdateRequestDto dto){
        return new ResponseEntity<>(commentService.modifyCommentById(id,dto),HttpStatus.OK);
    }

    @DeleteMapping("/{id}") // 댓글 삭제 메소드
    public ResponseEntity<Void> deleteCommentById(@PathVariable Long id){
        commentService.deleteCommentById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}

