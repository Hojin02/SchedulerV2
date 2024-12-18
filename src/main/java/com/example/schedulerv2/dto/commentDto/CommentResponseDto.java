package com.example.schedulerv2.dto.commentDto;

import com.example.schedulerv2.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponseDto {
    private final Long id;
    private final String scheduleIdAndTitle;
    private final String comments;
    private final String userEmail;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static CommentResponseDto toDto(Comment comment){
        return new CommentResponseDto(
                comment.getId(),
                "일정id: "+comment.getSchedule().getId() + " 일정제목: " + comment.getSchedule().getTitle(),
                comment.getContents(),
                comment.getUser().getEmail(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
