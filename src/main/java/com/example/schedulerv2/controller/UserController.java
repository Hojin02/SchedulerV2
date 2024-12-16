package com.example.schedulerv2.controller;

import com.example.schedulerv2.dto.userDto.UserResponseDto;
import com.example.schedulerv2.service.userService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> addUser(@RequestBody UserResponseDto dto){
        return new ResponseEntity<>(userService.addUser(dto), HttpStatus.CREATED);
    }


}
