package com.example.schedulerv2.service.commentService;

import com.example.schedulerv2.dto.commentDto.CommentRequest;
import com.example.schedulerv2.dto.commentDto.CommentResponseDto;
import com.example.schedulerv2.dto.commentDto.CommentUpdateRequestDto;
import com.example.schedulerv2.entity.Comment;
import com.example.schedulerv2.entity.Schedule;
import com.example.schedulerv2.entity.User;
import com.example.schedulerv2.error.CustomException;
import com.example.schedulerv2.error.errorCode.CommentErrorCode;
import com.example.schedulerv2.error.errorCode.ScheduleErrorCode;
import com.example.schedulerv2.error.errorCode.UserErrorCode;
import com.example.schedulerv2.repository.scheduleRepository.ScheduleRepository;
import com.example.schedulerv2.repository.userRepository.UserRepository;
import com.example.schedulerv2.repository.commentRepository.CommentReopsitory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final CommentReopsitory commentReopsitory;
    private final EntityManager em;
    private final HttpSession session;

    @Override
    public CommentResponseDto addComment(CommentRequest dto) {
        Schedule schedule = scheduleRepository.findById(dto.getScheduleId())
                .orElseThrow(() -> new CustomException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
        User user = userRepository.findByEmail((String) session.getAttribute("userEmail"))
                .orElseThrow(() -> new CustomException(UserErrorCode.LOGINED_USER_NOT_FOUND));
        Comment comment = commentReopsitory.save(
                new Comment(dto.getContents(), schedule, user)
        );
        return CommentResponseDto.toDto(comment);
    }

    @Override
    public CommentResponseDto findCommentById(Long id) {
        Comment comment = commentReopsitory.findById(id)
                .orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));
        return CommentResponseDto.toDto(comment);
    }

    @Override
    public List<CommentResponseDto> findAllComment() {
        List<Comment> comments = commentReopsitory.findAll();
        if (comments.isEmpty()) {
            throw new CustomException(CommentErrorCode.COMMENT_NOT_FOUND);
        }

        return comments.stream()
                .map(CommentResponseDto::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentResponseDto modifyCommentById(Long id, CommentUpdateRequestDto dto) {
        Comment comment = commentReopsitory.findById(id)
                .orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));
        comment.updateContents(dto.getContents());
        em.flush();
        return CommentResponseDto.toDto(comment);
    }

    @Override
    public void deleteCommentById(Long id) {
        Comment comment = commentReopsitory.findById(id)
                .orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));
        commentReopsitory.delete(comment);
    }
}
