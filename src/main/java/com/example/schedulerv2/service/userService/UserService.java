package com.example.schedulerv2.service.userService;

import com.example.schedulerv2.dto.userDto.UserRequestDto;
import com.example.schedulerv2.dto.userDto.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto addUser(UserRequestDto dto);

    List<UserResponseDto> findAllUser();

    UserResponseDto findUserById(Long id);

    UserResponseDto modifyUserById(Long id, UserRequestDto dto);

    void deleteUserById(Long id);
}
