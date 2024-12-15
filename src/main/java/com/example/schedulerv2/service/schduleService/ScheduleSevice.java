package com.example.schedulerv2.service.schduleService;

import com.example.schedulerv2.scheduleDto.ScheduleRequestDto;
import com.example.schedulerv2.scheduleDto.ScheduleResponseDto;

public interface ScheduleSevice {

    ScheduleResponseDto addSchedule(ScheduleRequestDto dto);
}
