package com.example.schedulerv2.service.schduleService;

import com.example.schedulerv2.scheduleDto.ScheduleRequestDto;
import com.example.schedulerv2.scheduleDto.ScheduleResponseDto;

import java.util.List;

public interface ScheduleSevice {

    ScheduleResponseDto addSchedule(ScheduleRequestDto dto);

    List<ScheduleResponseDto> findAllByTitleAndModifiedDate(String title, String updatedAt);
}
