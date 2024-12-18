package com.example.schedulerv2.dto.commentDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentRequest {
    @Positive
    private Long scheduleId;
    @NotBlank
    private String contents;

}
