package com.example.schedulerv2.entity;

import com.example.schedulerv2.dto.commentDto.CommentUpdateRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comment")
@NoArgsConstructor
@Getter
public class Comment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    @ManyToOne
    @JoinColumn(name = "schedule_id", referencedColumnName = "id", nullable = false)
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    public Comment(String contents, Schedule schedule, User user) {
        this.contents = contents;
        this.schedule = schedule;
        this.user = user;
    }

    public void updateContents(String contents) {
        this.contents = contents;
    }
}
