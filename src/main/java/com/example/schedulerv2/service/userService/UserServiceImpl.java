package com.example.schedulerv2.service.userService;

import com.example.schedulerv2.common.config.PasswordEncoder;
import com.example.schedulerv2.dto.userDto.UserLoginRequestDto;
import com.example.schedulerv2.dto.userDto.UserRequestDto;
import com.example.schedulerv2.dto.userDto.UserResponseDto;
import com.example.schedulerv2.entity.User;
import com.example.schedulerv2.common.error.CustomException;
import com.example.schedulerv2.common.error.errorCode.UserErrorCode;
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

    @Override //회원가입 메소드
    public UserResponseDto addUser(UserRequestDto dto) {
        // 이미 가입된 이메일일 경우 에러 처리.
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new CustomException(UserErrorCode.HAS_EMAIL);
        }
        // 사용자가 작성한 비밀번호 암호화
        String encodePassword = passwordEncoder.encode(dto.getPassword());
        User user = userRepository.save(
                new User(dto.getUserName(), dto.getEmail(), encodePassword)
        );
        // 생성된 유저 정보 DTO로 변환하여 반호나
        return UserResponseDto.toDto(user);
    }

    @Override // 로그인 메소드
    public void loginUser(UserLoginRequestDto dto) {
        // 사용자가 입력한 이메일로 해당 이메일의 유저 정보를 불러옴.
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new CustomException(UserErrorCode.INVALID_EMAIL_OR_PASSWORD));
        // 불러온 유저의 비밀번호화 사용자가 입력한 비밀번호가 일치하는지 화깅ㄴ
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new CustomException(UserErrorCode.INVALID_EMAIL_OR_PASSWORD);
        }
        // 세션에 유저 이메일을 저장.
        session.setAttribute("userEmail", user.getEmail());
    }

    // 로그아웃 시 세션을 무효화함.
    public void logout() {
        session.invalidate();
    }

    @Override // 유저정보 조회.
    public List<UserResponseDto> findAllUser() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserResponseDto::toDto)
                .collect(Collectors.toList());

    }

    @Override // 유저 단건 조회
    public UserResponseDto findUserById(Long id) {
        // id로 유저의 단건 정보 조회
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        return UserResponseDto.toDto(user);
    }

    @Transactional
    @Override // 유저 수정
    public UserResponseDto modifyUserById(Long id, UserRequestDto dto) {
        // id로 유저 뷸러옴.
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        // 유저 정보 수정
        user.updateUserNameAndEmail(dto.getUserName(), dto.getEmail());
        em.flush(); // 수정된 내용 즉시 반영
        // 수정된 정보 DTO로 변환하여 반환.
        return UserResponseDto.toDto(user);
    }

    @Override// 회원 탈퇴
    public void deleteUserById(Long id) {
        // 회원의 id로 유저정보 불러옴
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);// 유저삭제
        session.invalidate();// 삭제 시 세션도 무효화
    }

}
