package com.example.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    @JsonProperty("access_token")
    private String accessToken;
    private UserLoginResponse user;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserLoginResponse {
        private Long id;
        private String email;
        private String name;
    }
}
