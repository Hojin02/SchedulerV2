package com.example.schedulerv2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@NoArgsConstructor
@Getter
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    @Email
    private String email;

    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public void updateUserNameAndEmail(String userName, String email){
        this.userName = userName;
        this.email = email;
    }
}
