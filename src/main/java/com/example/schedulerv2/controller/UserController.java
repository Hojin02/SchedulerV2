package com.example.schedulerv2.controller;

import com.example.schedulerv2.dto.userDto.UserLoginRequestDto;
import com.example.schedulerv2.dto.userDto.UserRequestDto;
import com.example.schedulerv2.dto.userDto.UserResponseDto;
import com.example.schedulerv2.service.userService.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

//    @PostMapping
//    public ResponseEntity<UserResponseDto> addUser(@RequestBody UserRequestDto dto){
//        return new ResponseEntity<>(userService.addUser(dto), HttpStatus.CREATED);
//    }
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserRequestDto dto){
        return new ResponseEntity<>(userService.addUser(dto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> loginUser(@RequestBody UserLoginRequestDto dto,HttpSession session){
        userService.loginUser(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAllUser(){
        return new ResponseEntity<>(userService.findAllUser(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable Long id){
        return new ResponseEntity<>(userService.findUserById(id),HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> modifyUserById(@PathVariable Long id, @RequestBody UserRequestDto dto){
        return new ResponseEntity<>(userService.modifyUserById(id,dto),HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id){
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void>  logoutUser(){
        System.out.println("??????????");
        userService.logout();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
