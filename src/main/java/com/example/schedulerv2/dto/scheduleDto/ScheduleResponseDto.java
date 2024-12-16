package com.example.schedulerv2.dto.scheduleDto;

import com.example.schedulerv2.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
@AllArgsConstructor
@Getter

public class ScheduleResponseDto {
    private final Long id;
    private final String userName;
    private final String title;
    private final String contents;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ScheduleResponseDto toDto(Schedule schedule){
        return new ScheduleResponseDto(
                schedule.getId(),
                schedule.getUser().getUserName(),
                schedule.getTitle(),
                schedule.getContents(),
                schedule.getCreatedAt(),
                schedule.getUpdatedAt()
        );
    }
}
