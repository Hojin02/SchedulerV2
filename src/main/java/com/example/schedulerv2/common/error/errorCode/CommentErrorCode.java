package com.example.schedulerv2.common.error.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommentErrorCode implements ErrorCode {
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT-001", "해당 일정을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
