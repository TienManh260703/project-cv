package com.example.dto.response;

import com.example.entity.User;
import com.example.util.constant.GenderEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    Long id;
    String name;
    String email;
    Integer age;
    GenderEnum gender;
    String address;
    @JsonProperty("refresh_token")
    String refreshToken;
    @JsonProperty( "created_at")
    Instant createdAt;
    @JsonProperty( "updated_at")
    Instant updatedAt;
    @JsonProperty( "created_by")
    String createdBy;
    @JsonProperty( "updated_by")
    String updatedBy;

    public static UserResponse transUser (User user){
        return  UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .age(user.getAge())
                .email(user.getEmail())
                .gender(user.getGender())
                .address(user.getAddress())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .createdBy(user.getCreatedBy())
                .updatedBy(user.getUpdatedBy())
                .build();
    }
}
