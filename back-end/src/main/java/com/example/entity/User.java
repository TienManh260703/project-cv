package com.example.entity;

import com.example.service.security.SecurityService;
import com.example.util.constant.GenderEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String email;
    String password;
    Integer age;
    @Enumerated(EnumType.STRING)
    GenderEnum gender;
    String address;
    @Column(name = "refresh_token",columnDefinition = "MEDIUMTEXT")
    String refreshToken;
    @Column(name = "created_at")
    Instant createdAt;
    @Column(name = "updated_at")
    Instant updatedAt;
    @Column(name = "created_by")
    String createdBy;
    @Column(name = "updated_by")
    String updatedBy;

    @PrePersist
    private void handleBeforeCreateAt() {
        this.createdBy = SecurityService.getCurrentUserLogin().isPresent() == true ?
                SecurityService.getCurrentUserLogin().get()
                : "";
        this.createdAt = Instant.now();
    }

    @PreUpdate
    private void handBeforeUpdateAt() {
        this.updatedAt = Instant.now();
        this.updatedBy = SecurityService.getCurrentUserLogin().isPresent() == true ?
                SecurityService.getCurrentUserLogin().get()
                : "";
    }
}
