package com.example.schedulerv2.entity;

import com.example.schedulerv2.dto.scheduleDto.ScheduleUpdateRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "schedule")
@NoArgsConstructor
@Getter
public class Schedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "longtext")
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    public Schedule(String title, String contents,User user) {
        this.title = title;
        this.contents = contents;
        this.user = user;
    }

    public void UpdateTitleAndContents(ScheduleUpdateRequestDto dto) {
        this.title = dto.getTitle();
        this.contents = dto.getContents();
    }

}
