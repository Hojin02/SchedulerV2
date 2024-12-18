package com.example.schedulerv2.service.commentService;

import com.example.schedulerv2.dto.commentDto.CommentRequest;
import com.example.schedulerv2.dto.commentDto.CommentResponseDto;
import com.example.schedulerv2.dto.userDto.UserResponseDto;
import com.example.schedulerv2.entity.Comment;
import com.example.schedulerv2.entity.Schedule;
import com.example.schedulerv2.entity.User;
import com.example.schedulerv2.repository.scheduleRepository.ScheduleRepository;
import com.example.schedulerv2.repository.userRepository.UserRepository;
import com.example.schedulerv2.service.commentRepository.CommentReopsitory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final CommentReopsitory commentReopsitory;
    private final EntityManager em;
    private final HttpSession session;
    @Override
    public CommentResponseDto addComment(CommentRequest dto) {
        Schedule schedule = scheduleRepository.findByIdOrElseThrow(dto.getScheduleId());
        User user = userRepository.findUserByEmail((String) session.getAttribute("userEmail"));
        Comment comment = commentReopsitory.save(
                new Comment(dto.getContents(), schedule,user)
        );
        return CommentResponseDto.toDto(comment);
    }
}
