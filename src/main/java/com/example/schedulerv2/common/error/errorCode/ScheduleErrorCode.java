package com.example.schedulerv2.common.error.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ScheduleErrorCode implements ErrorCode {
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "SCHEDULE-001", "해당 일정을 찾을 수 없습니다."),
    SCHEDULE_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "SCHEDULE-002", "본인이 작성한 일정만 수정 및 삭제 할 수 있습니다.");

    private final HttpStatus httpStatus;	// HttpStatus
    private final String code;				// ACCOUNT-001
    private final String message;			// 설명

}
