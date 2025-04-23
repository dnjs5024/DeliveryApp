package com.example.delivery.domain.user.entity;
import com.example.delivery.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private LocalDateTime withdrawTime;

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

    public void withdraw() {
        this.withdrawTime = LocalDateTime.now();
    }

    public void changeRole(Role newRole) {
        this.role = newRole;
    }


}
