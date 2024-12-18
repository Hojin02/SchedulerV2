package com.example.schedulerv2.dto.commentDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentUpdateRequestDto {
    @NotBlank
    private String contents;
}
