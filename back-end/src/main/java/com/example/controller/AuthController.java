package com.example.controller;

import com.example.dto.request.LoginRequest;
import com.example.dto.response.LoginResponse;
import com.example.entity.User;
import com.example.exception.DataNoFoundException;
import com.example.service.UserService;
import com.example.service.security.SecurityService;
import com.example.util.annotation.ApiMessage;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthController {

    @Value("${manh-nt.jwt.refresh-token-validity-in-seconds}")
    Long refreshTokenExpiration;

    final AuthenticationManagerBuilder authenticationManagerBuilder;
    final SecurityService securityService;
    final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) throws DataNoFoundException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );
//        Xác thực người dùng => build function loadUserByUserName
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // create token
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LoginResponse loginResponse = new LoginResponse();
        User currentUser = this.userService.getUserByEmail(request.getUsername());
        if (currentUser != null) {
            LoginResponse.UserLoginResponse userLogin =
                    new LoginResponse.UserLoginResponse(currentUser.getId(), currentUser.getEmail(), currentUser.getName());
            loginResponse.setUser(userLogin);
        }
        String accessToken = this.securityService.createAccessToken(authentication , loginResponse.getUser());
        loginResponse.setAccessToken(accessToken);

        // refresh token
        String refreshToken = this.securityService.refreshToken(request.getUsername(), loginResponse);
        // update User
        this.userService.updateUserToken(refreshToken, request.getUsername());
        // set cookies
        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
//                .domain("")
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(loginResponse);
    }

    @GetMapping("/account")
    @ApiMessage("Get Account User")
    public ResponseEntity<LoginResponse.UserLoginResponse> getAccount() throws DataNoFoundException {
        String email =SecurityService.getCurrentUserLogin().isPresent()
                ? SecurityService.getCurrentUserLogin().get() : "";
        User currentUser = this.userService.getUserByEmail(email);
        LoginResponse.UserLoginResponse userLogin = new LoginResponse.UserLoginResponse();
        if (currentUser != null) {
           userLogin.setId(currentUser.getId());
           userLogin.setEmail(currentUser.getEmail());
           userLogin.setName(currentUser.getName());
        }
        return ResponseEntity.status(HttpStatus.OK).body(userLogin);
    }
}
