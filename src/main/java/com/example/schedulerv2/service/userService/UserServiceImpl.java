package com.example.schedulerv2.service.userService;

import com.example.schedulerv2.dto.userDto.UserResponseDto;
import com.example.schedulerv2.entity.User;
import com.example.schedulerv2.repository.userRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<UserResponseDto> findAllUser() {
        List<User> users= userRepository.findAll();
        return  users.stream()
                .map(UserResponseDto::toDto)
                .collect(Collectors.toList());

    }

    @Override
    public UserResponseDto findUserById(Long id) {
        User user = userRepository.findByIdOrElseThrow(id);
        return UserResponseDto.toDto(user);
    }
}
