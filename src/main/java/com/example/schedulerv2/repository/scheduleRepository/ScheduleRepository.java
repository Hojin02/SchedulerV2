package com.example.schedulerv2.repository.scheduleRepository;


import com.example.schedulerv2.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

}
