package com.example.schedulerv2.service.schduleService;

import com.example.schedulerv2.dto.scheduleDto.ScheduleRequestDto;
import com.example.schedulerv2.dto.scheduleDto.ScheduleResponseDto;
import com.example.schedulerv2.dto.scheduleDto.ScheduleUpdateRequestDto;

import java.util.List;

public interface ScheduleService {

    ScheduleResponseDto addSchedule(ScheduleRequestDto dto);

//    List<ScheduleResponseDto> findAllByTitleAndModifiedDate(String title, String updatedAt);
//
//
//    ScheduleResponseDto findScheduleById(Long id);
//
//    ScheduleResponseDto modifyScheduleById(Long id,ScheduleUpdateRequestDto dto);
//
//    void deleteScheduleById(Long id);

}
