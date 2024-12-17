package com.example.schedulerv2.dto.scheduleDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleRequestDto {
    @NotBlank
    @Size(max=10)
    private final String title;
    @NotBlank
    private final String contents;
}
