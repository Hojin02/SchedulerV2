package com.example.schedulerv2.repository.scheduleRepository;


import com.example.schedulerv2.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

    @Query("SELECT s FROM Schedule s WHERE "
            + "(:title IS NULL OR s.title LIKE %:title%) "
            + "AND (:updatedAt IS NULL OR DATE(s.updatedAt) = :updatedAt)")
    List<Schedule> findAllByTitleAndUpdatedAt(
            @Param("title") String title,
            @Param("updatedAt") LocalDate updatedAt
    );
}
