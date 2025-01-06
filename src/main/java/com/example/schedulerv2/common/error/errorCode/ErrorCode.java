package com.example.schedulerv2.common.error.errorCode;


import org.springframework.http.HttpStatus;

public interface ErrorCode {
    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();

}
