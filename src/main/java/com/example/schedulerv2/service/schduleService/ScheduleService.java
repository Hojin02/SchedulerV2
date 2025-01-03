package com.example.schedulerv2.service.schduleService;

import com.example.schedulerv2.dto.scheduleDto.ScheduleRequestDto;
import com.example.schedulerv2.dto.scheduleDto.ScheduleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScheduleService {

    ScheduleResponseDto addSchedule(ScheduleRequestDto dto);

    Page<ScheduleResponseDto> findAllByTitleAndModifiedDate(String title, String updatedAt, Pageable pageable);


    ScheduleResponseDto findScheduleById(Long id);

    ScheduleResponseDto modifyScheduleById(Long id,ScheduleRequestDto dto);

    void deleteScheduleById(Long id);

}
