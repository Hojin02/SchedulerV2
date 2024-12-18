package com.example.schedulerv2.service.schduleService;

import com.example.schedulerv2.entity.Schedule;
import com.example.schedulerv2.entity.User;
import com.example.schedulerv2.repository.scheduleRepository.ScheduleRepository;
import com.example.schedulerv2.dto.scheduleDto.ScheduleRequestDto;
import com.example.schedulerv2.dto.scheduleDto.ScheduleResponseDto;
import com.example.schedulerv2.repository.userRepository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final EntityManager em;
    private final HttpSession session;

    @Override
    public ScheduleResponseDto addSchedule(ScheduleRequestDto dto) {
        User user = userRepository.findUserByEmail((String)session.getAttribute("userEmail"));
        Schedule savedSchedule = scheduleRepository.save(
                new Schedule(dto.getTitle(), dto.getContents(),user)
        );
        ScheduleResponseDto resultDto = ScheduleResponseDto.toDto(savedSchedule);
        return resultDto;
    }

    @Override
    public Page<ScheduleResponseDto> findAllByTitleAndModifiedDate(String title, String updatedAt, Pageable pageable) {
        LocalDate localDate = null;

        if (updatedAt != null && !updatedAt.isBlank()) {
            localDate = LocalDate.parse(updatedAt);
        }
        Page<Schedule> schedules = scheduleRepository.findAllByTitleAndUpdatedAt(title, localDate,pageable);

        return schedules.map(ScheduleResponseDto::toDto);
    }

    @Override
    public ScheduleResponseDto findScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findByIdOrElseThrow(id);
        return ScheduleResponseDto.toDto(schedule);
    }

    @Override
    @Transactional
    public ScheduleResponseDto modifyScheduleById(Long id, ScheduleRequestDto dto) {
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
