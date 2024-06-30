package com.example.entity;

import com.example.service.security.SecurityService;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String logo;
    String address;
    @Column(columnDefinition = "MEDIUMTEXT")
    String description;
    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GTM+7")
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
