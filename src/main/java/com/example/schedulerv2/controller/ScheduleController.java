package com.example.schedulerv2.controller;

import com.example.schedulerv2.dto.scheduleDto.ScheduleRequestDto;
import com.example.schedulerv2.dto.scheduleDto.ScheduleResponseDto;
import com.example.schedulerv2.service.schduleService.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> addSchedule(@Valid  @RequestBody ScheduleRequestDto dto){
        return new ResponseEntity<>(scheduleService.addSchedule(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ScheduleResponseDto>>findAllByTitleAndModifiedDate(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String updatedAt,
            @PageableDefault(size = 10) Pageable pageable
    ){
        return new ResponseEntity<>(scheduleService.findAllByTitleAndModifiedDate(title,updatedAt,pageable),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(@PathVariable Long id){
        return new ResponseEntity<>(scheduleService.findScheduleById(id),HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> modifyScheduleById(@PathVariable Long id,@Valid @RequestBody ScheduleRequestDto dto){
        return new ResponseEntity<>(scheduleService.modifyScheduleById(id,dto),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScheduleById(@PathVariable Long id){
        scheduleService.deleteScheduleById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
