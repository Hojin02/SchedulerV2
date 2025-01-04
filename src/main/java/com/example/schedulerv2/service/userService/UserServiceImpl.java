package com.example.schedulerv2.service.userService;

import com.example.schedulerv2.config.PasswordEncoder;
import com.example.schedulerv2.dto.userDto.UserLoginRequestDto;
import com.example.schedulerv2.dto.userDto.UserRequestDto;
import com.example.schedulerv2.dto.userDto.UserResponseDto;
import com.example.schedulerv2.entity.User;
import com.example.schedulerv2.error.CustomException;
import com.example.schedulerv2.error.errorCode.UserErrorCode;
import com.example.schedulerv2.repository.userRepository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EntityManager em;
    private final HttpSession session;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto addUser(UserRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new CustomException(UserErrorCode.HAS_EMAIL);
        }
        String encodePassword = passwordEncoder.encode(dto.getPassword());
        User user = userRepository.save(
                new User(dto.getUserName(), dto.getEmail(), encodePassword)
        );
        return UserResponseDto.toDto(user);
    }

    @Override
    public void loginUser(UserLoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new CustomException(UserErrorCode.INVALID_EMAIL_OR_PASSWORD));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new CustomException(UserErrorCode.INVALID_EMAIL_OR_PASSWORD);
        }
        session.setAttribute("userEmail", user.getEmail());
    }


    public void logout() {
        session.invalidate();
    }

    @Override
    public List<UserResponseDto> findAllUser() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserResponseDto::toDto)
                .collect(Collectors.toList());

    }

    @Override
    public UserResponseDto findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        return UserResponseDto.toDto(user);
    }

    @Transactional
    @Override
    public UserResponseDto modifyUserById(Long id, UserRequestDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        user.updateUserNameAndEmail(dto.getUserName(), dto.getEmail());
        em.flush();
        return UserResponseDto.toDto(user);
    }

    @Override
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
        session.invalidate();
    }

}
