package com.example.schedulerv2.service.userService;

import com.example.schedulerv2.config.PasswordEncoder;
import com.example.schedulerv2.dto.userDto.UserLoginRequestDto;
import com.example.schedulerv2.dto.userDto.UserRequestDto;
import com.example.schedulerv2.dto.userDto.UserResponseDto;
import com.example.schedulerv2.entity.User;
import com.example.schedulerv2.repository.userRepository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
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
        if(userRepository.existsByEmail(dto.getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"This email already exists.");
        }
        String encodePassword=passwordEncoder.encode(dto.getPassword());
        User user = userRepository.save(
                new User(dto.getUserName(), dto.getEmail(), encodePassword)
        );
        return UserResponseDto.toDto(user);
    }

    @Override
    public void loginUser(UserLoginRequestDto dto) {
        Optional<User> optionalUser = userRepository.findByEmail(dto.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(dto.getPassword(),user.getPassword())) {
                session.setAttribute("userEmail",user.getEmail());
            }
        }else{
           session.invalidate();
        }
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
        User user = userRepository.findByIdOrElseThrow(id);
        return UserResponseDto.toDto(user);
    }

    @Transactional
    @Override
    public UserResponseDto modifyUserById(Long id, UserRequestDto dto) {
        User user = userRepository.findByIdOrElseThrow(id);
        user.updateUserNameAndEmail(dto.getUserName(), dto.getEmail());
        em.flush();
        return UserResponseDto.toDto(user);
    }

    @Override
    public void deleteUserById(Long id) {
        User user = userRepository.findByIdOrElseThrow(id);
        userRepository.delete(user);
        session.invalidate();
    }

}
