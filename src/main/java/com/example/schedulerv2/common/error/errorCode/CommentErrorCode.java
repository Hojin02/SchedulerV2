package com.example.schedulerv2.common.error.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommentErrorCode implements ErrorCode {
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT-001", "해당 일정을 찾을 수 없습니다."),
    COMMENT_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "COMMENT-002", "본인이 작성한 댓글만 수정 및 삭제 할 수 있습니다.");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
