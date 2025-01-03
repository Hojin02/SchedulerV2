package com.example.schedulerv2.repository.scheduleRepository;


import com.example.schedulerv2.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

    @Query("SELECT s FROM Schedule s WHERE "
            + "(:title IS NULL OR s.title LIKE %:title%) "
            + "AND (:updatedAt IS NULL OR DATE(s.updatedAt) = :updatedAt)"
            + "ORDER BY s.updatedAt DESC")
    Page<Schedule> findAllByTitleAndUpdatedAt(
            @Param("title") String title,
            @Param("updatedAt") LocalDate updatedAt,
            Pageable pageable
    );

}
