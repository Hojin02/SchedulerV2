package com.example.schedulerv2.service.userService;

import com.example.schedulerv2.dto.userDto.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto addUser(UserResponseDto dto);

    List<UserResponseDto> findAllUser();

    UserResponseDto findUserById(Long id);
}
