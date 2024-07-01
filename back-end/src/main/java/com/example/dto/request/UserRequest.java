package com.example.dto.request;

import com.example.util.constant.GenderEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    Long id;
    @NotBlank(message = "Name cannot be blank")
    String name;
    @NotBlank(message = "Email cannot be blank")
    String email;
    @NotBlank(message = "Password cannot be blank")
    String password;
    @Pattern(regexp = "^[0-9]+$", message = "Age must be an integer")
    @Min(value = 0, message = "Age must be greater than zero")
    @Max(value = 150, message = "Age must not exceed 150")
    Integer age;
    GenderEnum gender;
    String address;
//    @JsonProperty("refresh_token")
//    String refreshToken;
}
