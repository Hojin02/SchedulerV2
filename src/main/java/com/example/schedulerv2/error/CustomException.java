package com.example.schedulerv2.error;

import com.example.schedulerv2.error.errorCode.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException  extends RuntimeException  {
   ErrorCode errorCode;
}
