package com.example.schedulerv2.dto.userDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginRequestDto {
    @NotBlank
    private final String email;
    @NotBlank
    private final String password;
}
