package com.example.schedulerv2.service.schduleService;

import com.example.schedulerv2.entity.Schedule;
import com.example.schedulerv2.repository.scheduleRepository.ScheduleRepository;
import com.example.schedulerv2.scheduleDto.ScheduleRequestDto;
import com.example.schedulerv2.scheduleDto.ScheduleResponseDto;
import com.example.schedulerv2.scheduleDto.ScheduleUpdateRequestDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final EntityManager em;

    @Override
    public ScheduleResponseDto addSchedule(ScheduleRequestDto dto) {
        Schedule savedSchedule = scheduleRepository.save(
                new Schedule(dto.getUserName(), dto.getTitle(), dto.getContents())
        );
        return ScheduleResponseDto.toDto(savedSchedule);
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

    @Override
    public ScheduleResponseDto findScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findByIdOrElseThrow(id);
        return ScheduleResponseDto.toDto(schedule);
    }

    @Override
    @Transactional
    public ScheduleResponseDto modifyScheduleById(Long id, ScheduleUpdateRequestDto dto) {
        Schedule schedule = scheduleRepository.findByIdOrElseThrow(id);
        schedule.UpdateTitleAndContents(dto);
        em.flush();//변경사항 즉시 반영
        return ScheduleResponseDto.toDto(schedule);
    }

    @Override
    public void deleteScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findByIdOrElseThrow(id);
        scheduleRepository.delete(schedule);
    }
}
