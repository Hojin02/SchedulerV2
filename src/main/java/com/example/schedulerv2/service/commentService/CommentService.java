package com.example.schedulerv2.service.commentService;

import com.example.schedulerv2.dto.commentDto.CommentRequest;
import com.example.schedulerv2.dto.commentDto.CommentResponseDto;
import com.example.schedulerv2.dto.commentDto.CommentUpdateRequestDto;

import java.util.List;

public interface CommentService {
    CommentResponseDto addComment(CommentRequest dto);

    CommentResponseDto findCommentById(Long id);

    List<CommentResponseDto> findAllComment();

    CommentResponseDto modifyCommentById(Long id, CommentUpdateRequestDto dto);

    void deleteCommentById(Long id);
}
