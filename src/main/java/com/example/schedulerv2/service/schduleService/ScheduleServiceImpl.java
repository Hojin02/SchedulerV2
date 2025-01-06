package com.example.schedulerv2.service.schduleService;

import com.example.schedulerv2.entity.Schedule;
import com.example.schedulerv2.entity.User;
import com.example.schedulerv2.common.error.CustomException;
import com.example.schedulerv2.common.error.errorCode.ScheduleErrorCode;
import com.example.schedulerv2.common.error.errorCode.UserErrorCode;
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

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final EntityManager em;
    private final HttpSession session;

    @Override // 일정 추가 메소드
    public ScheduleResponseDto addSchedule(ScheduleRequestDto dto) {
        // 세션에서 로그인 된 유저의 id를 가져와 유저 불러옴.
        User user = userRepository.findByEmail((String) session.getAttribute("userEmail"))
                .orElseThrow(() -> new CustomException(UserErrorCode.LOGINED_USER_NOT_FOUND));
        // 유저와 일정 정보를 넣어 데이터 베이스에 저장.
        Schedule savedSchedule = scheduleRepository.save(
                new Schedule(dto.getTitle(), dto.getContents(), user)
        );
        // 일정 엔티티를 DTO타입으로 변환
        ScheduleResponseDto resultDto = ScheduleResponseDto.toDto(savedSchedule);
        return resultDto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ScheduleResponseDto> findAllByTitleAndModifiedDate(String title, String updatedAt, Pageable pageable) {
        LocalDate localDate = null;
        // 파라미터로 수정일이 넘어올경우 LocalDate으로 형변환.
        if (updatedAt != null && !updatedAt.isBlank()) {
            localDate = LocalDate.parse(updatedAt);
        } // 수정일과 일정 제목으로 필터링 하여 일정 전체 조회(페이징)
        Page<Schedule> schedules = scheduleRepository.findAllByTitleAndUpdatedAt(title, localDate, pageable);

        return schedules.map(ScheduleResponseDto::toDto);
    }

    @Override// 일정 단건조회
    @Transactional(readOnly = true)
    public ScheduleResponseDto findScheduleById(Long id) {
        //일정 id를 이용하여 불러옴
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
        // 조회된 일정을 DTO타입으로 변환화여 반환
        return ScheduleResponseDto.toDto(schedule);
    }

    @Override
    @Transactional //일정 수정
    public ScheduleResponseDto modifyScheduleById(Long id, ScheduleRequestDto dto) {
        // 일정 id로 일정 불러옴.
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
        schedule.UpdateTitleAndContents(dto);
        em.flush();//변경사항 즉시 반영
        return ScheduleResponseDto.toDto(schedule);
    }

    @Override // 일정 삭제
    public void deleteScheduleById(Long id) {
        // 일정 id로 일정 불러옴.
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
        // 일정 삭제.
        scheduleRepository.delete(schedule);

    }


}
