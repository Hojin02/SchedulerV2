package com.example.schedulerv2.service.commentService;

import com.example.schedulerv2.dto.commentDto.CommentRequest;
import com.example.schedulerv2.dto.commentDto.CommentResponseDto;
import com.example.schedulerv2.dto.commentDto.CommentUpdateRequestDto;
import com.example.schedulerv2.entity.Comment;
import com.example.schedulerv2.entity.Schedule;
import com.example.schedulerv2.entity.User;
import com.example.schedulerv2.common.error.CustomException;
import com.example.schedulerv2.common.error.errorCode.CommentErrorCode;
import com.example.schedulerv2.common.error.errorCode.ScheduleErrorCode;
import com.example.schedulerv2.common.error.errorCode.UserErrorCode;
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
    public CommentResponseDto addComment(CommentRequest dto) {// 댓글 추가
        // 일정 id로 일정 가져옴.
        Schedule schedule = scheduleRepository.findById(dto.getScheduleId())
                .orElseThrow(() -> new CustomException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
        // 로그인 된 유저의 이메일로 유저 가져옴.
        User user = userRepository.findByEmail((String) session.getAttribute("userEmail"))
                .orElseThrow(() -> new CustomException(UserErrorCode.LOGINED_USER_NOT_FOUND));
        // 댓글 추가(댓글내용,일정,유저)
        Comment comment = commentReopsitory.save(
                new Comment(dto.getContents(), schedule, user)
        );
        // 추가된 댓글 정보 반환
        return CommentResponseDto.toDto(comment);
    }

    @Override// 댓글 단건 조회
    @Transactional(readOnly = true)
    public CommentResponseDto findCommentById(Long id) {
        // 댓글 아이디로 해당 댓글 정보 불러옴.
        Comment comment = commentReopsitory.findById(id)
                .orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));
        // 단건의 댓글 정보를 DTO로 변환하여 반환
        return CommentResponseDto.toDto(comment);
    }

    @Override // 전체 댓글 조회
    @Transactional(readOnly = true)
    public List<CommentResponseDto> findAllComment() {
        List<Comment> comments = commentReopsitory.findAll();
        if (comments.isEmpty()) { // 조회된 댓글이 없을 시 에러 처리
            throw new CustomException(CommentErrorCode.COMMENT_NOT_FOUND);
        }
        // 댓글 목록을 반환
        return comments.stream()
                .map(CommentResponseDto::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional // 댓글 수정 메소드
    public CommentResponseDto modifyCommentById(Long id, CommentUpdateRequestDto dto) {
        Comment comment = ownerCheck(id);//본인이 작성한 댓글이 아닐경우 에러처리
        // 댓글 수정
        comment.updateContents(dto.getContents());
        em.flush(); // 수정된 내용 즉시 반영
        // 수정된 댓글 정보 반환
        return CommentResponseDto.toDto(comment);
    }

    @Override // 댓글 삭제
    public void deleteCommentById(Long id) {
        Comment comment = ownerCheck(id);//본인이 작성한 댓글이 아닐경우 에러처리
        commentReopsitory.delete(comment); // 댓글 삭제
    }

    public Comment ownerCheck(Long commentId){
        // 세션에서 로그인 된 유저의 id를 가져와 유저 불러옴.
        User user = userRepository.findByEmail((String) session.getAttribute("userEmail"))
                .orElseThrow(() -> new CustomException(UserErrorCode.LOGINED_USER_NOT_FOUND));
        // 댓글 id로 댓글 불러옴.
        Comment comment = commentReopsitory.findById(commentId) // 댓글 id로 댓글 불러옴
                .orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));

        // 댓글정보의 유저와 로그인 한 유저가 같은 지 확인.
        if(!comment.getUser().equals(user)){
            throw new CustomException((CommentErrorCode.COMMENT_PERMISSION_DENIED));
        }
        return comment;
    }
}
