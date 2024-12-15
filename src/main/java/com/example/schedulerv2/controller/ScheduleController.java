package com.example.schedulerv2.controller;

import com.example.schedulerv2.scheduleDto.ScheduleRequestDto;
import com.example.schedulerv2.scheduleDto.ScheduleResponseDto;
import com.example.schedulerv2.service.schduleService.ScheduleSevice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {
    private final ScheduleSevice scheduleSevice;
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> addSchedule(@RequestBody ScheduleRequestDto dto){
        return new ResponseEntity<>(scheduleSevice.addSchedule(dto), HttpStatus.OK);
    }

}
