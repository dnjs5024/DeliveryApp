package com.example.delivery.user.entity;
import com.example.delivery.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Entity
@Table(name = "users")
@AllArgsConstructor
public class User extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Setter
    private LocalDateTime withdrawTime;

    @Setter
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public User() {
    }

    public enum Role {
        USER,
        OWNER
    }

    public User(String email, String password, Role role, String username) {
        this.email = email;
        this.username = username;
        this.role = role;
        this.password = password;
    }
}
