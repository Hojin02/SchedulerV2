package com.example.schedulerv2.repository.userRepository;

import com.example.schedulerv2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);//해당 이메일이 존재하는지 확인.
}
