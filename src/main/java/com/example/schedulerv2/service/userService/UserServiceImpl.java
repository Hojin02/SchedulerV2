package com.example.schedulerv2.service.userService;

import com.example.schedulerv2.dto.userDto.UserResponseDto;
import com.example.schedulerv2.entity.User;
import com.example.schedulerv2.repository.userRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    @Override
    public UserResponseDto addUser(UserResponseDto dto) {
        User user = userRepository.save(
                new User(dto.getUserName(),dto.getEmail())
        );
        return UserResponseDto.toDto(user);
    }

}
