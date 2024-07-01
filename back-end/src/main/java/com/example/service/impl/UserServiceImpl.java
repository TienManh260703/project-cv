package com.example.service.impl;

import com.example.dto.request.CreateUserRequest;
import com.example.dto.request.UpdateUserRequest;
import com.example.dto.response.Meta;
import com.example.dto.response.ResultPaginationResponse;
import com.example.dto.response.UserResponse;
import com.example.entity.User;
import com.example.exception.AppException;
import com.example.exception.DataNoFoundException;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public ResultPaginationResponse getUserPage(Optional<String> current, Optional<String> pageSize) {
        String sCurrent = current.isPresent() ? current.get() : "0";
        String sPageSize = pageSize.isPresent() ? pageSize.get() : "5";

        Pageable pageable = PageRequest.of(Integer.parseInt(sCurrent) - 1, Integer.parseInt(sPageSize));
        Page<User> userPage = userRepository.findAll(pageable);

        Meta meta = Meta.builder()
                .page(userPage.getNumber() + 1)
                .pageSize(userPage.getSize())
                .pages(userPage.getTotalPages())
                .total(userPage.getTotalElements())
                .build();

        ResultPaginationResponse response = ResultPaginationResponse
                .builder()
                .meta(meta)
                .result(userPage.getContent())
                .build();
        return response;
    }

    @Override
    public ResultPaginationResponse getUserFilter(Specification<User> spec, Pageable pageable) {
        Page<User> userPage = userRepository.findAll(spec, pageable);
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(userPage.getTotalPages())
                .total(userPage.getTotalElements())
                .build();
        ResultPaginationResponse response = ResultPaginationResponse
                .builder()
                .meta(meta)
                .result(userPage.getContent())
                .build();
        return response;
    }

    @Override
    public UserResponse getUser(Long id) throws DataNoFoundException {
        User existingUser = userRepository.findById(id).orElseThrow(
                () -> new DataNoFoundException("Data not found"));
        return UserResponse.transUser(existingUser);
    }

    @Override
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(
                user -> UserResponse.transUser(user)
        ).toList();
    }

    // log ra thông báo chung chung để ko bị lộ-> cần sửa lại
    @Override
    public User getUserByEmail(String email) throws DataNoFoundException {
        User existingUser = userRepository.findByEmail(email).orElseThrow(() -> (
                new DataNoFoundException("Cannot find user with email : " + email)
        ));
        return existingUser;
    }

    @Override
    public UserResponse create(CreateUserRequest request) throws AppException {
        if (this.userRepository.existsByEmail(request.getEmail())) {
            throw new AppException("Email " + request.getEmail() + " already exists");
        }
        User response = userRepository.save(convertCreateUserRequestToUser(request));
        return UserResponse.transUser(response);
    }

    @Override
    public UserResponse update(UpdateUserRequest request) throws DataNoFoundException {
        User existingUser = userRepository.findById(request.getId()).orElseThrow(
                () -> new DataNoFoundException("Data not found"));
        existingUser.setName(request.getName());
        existingUser.setAge(request.getAge());
        existingUser.setAddress(request.getAddress());
        existingUser.setGender(request.getGender());

        User response = userRepository.save(existingUser);
        return UserResponse.transUser(response);
    }

    @Override
    public void deleted(Long id) throws DataNoFoundException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.deleteById(id);
            return;
        }
        throw new DataNoFoundException("Cannot find user with id : " + id);
    }

    private User convertCreateUserRequestToUser(CreateUserRequest request) {
        String hashPassword = this.passwordEncoder.encode(request.getPassword());
        return User
                .builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(hashPassword)
                .gender(request.getGender())
                .address(request.getAddress())
                .age(request.getAge())
                .build();
    }

    private User convertUpdateUserRequestToUser(UpdateUserRequest request) {
        return User
                .builder()
                .id(request.getId())
                .name(request.getName())
                .age(request.getAge())
                .gender(request.getGender())
                .address(request.getAddress())
                .build();
    }
}
