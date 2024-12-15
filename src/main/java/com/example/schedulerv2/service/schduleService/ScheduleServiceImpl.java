package com.example.schedulerv2.service.schduleService;

import com.example.schedulerv2.entity.Schedule;
import com.example.schedulerv2.repository.scheduleRepository.ScheduleRepository;
import com.example.schedulerv2.scheduleDto.ScheduleRequestDto;
import com.example.schedulerv2.scheduleDto.ScheduleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleSevice {
    private final ScheduleRepository scheduleRepository;

    @Override
    public ScheduleResponseDto addSchedule(ScheduleRequestDto dto) {
        Schedule savedSchedule = scheduleRepository.save(
                new Schedule(dto.getUserName(), dto.getTitle(), dto.getContents())
        );
        return new ScheduleResponseDto(
                savedSchedule.getId(),
                savedSchedule.getUserName(),
                savedSchedule.getTitle(),
                savedSchedule.getContents(),
                savedSchedule.getCreatedAt(),
                savedSchedule.getUpdatedAt());
    }

    @Override
    public List<ScheduleResponseDto> findAllByTitleAndModifiedDate(String title, String updatedAt) {
        LocalDate localDate = null;

        if (updatedAt != null && !updatedAt.isBlank()) {
            localDate = LocalDate.parse(updatedAt);
        }
        List<Schedule> schedules = scheduleRepository.findAllByTitleAndUpdatedAt(title, localDate);

        return schedules.stream()
                .map(ScheduleResponseDto::toDto)
                .collect(Collectors.toList());
    }
}
