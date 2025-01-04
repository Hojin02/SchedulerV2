package com.example.schedulerv2.error.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "ACCOUNT-001", "사용자를 찾을 수 없습니다."),
    HAS_EMAIL(HttpStatus.BAD_REQUEST, "ACCOUNT-002", "이미 존재하는 이메일입니다."),
    INVALID_EMAIL_OR_PASSWORD(HttpStatus.BAD_REQUEST, "ACCOUNT-003", "이메일 또는 비밀번호가 일치하지 않습니다."),
    LOGINED_USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "ACCOUNT-004", "로그인한 사용자를 찾을 수 없습니다. 다시 로그인 해주세요.");

    private final HttpStatus httpStatus;	// HttpStatus
    private final String code;				// ACCOUNT-001
    private final String message;			// 설명

}
