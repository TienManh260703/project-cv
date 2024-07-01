package com.example.service;

import com.example.dto.request.CreateUserRequest;
import com.example.dto.request.UpdateUserRequest;
import com.example.dto.response.ResultPaginationResponse;
import com.example.dto.response.UserResponse;
import com.example.entity.User;
import com.example.exception.AppException;
import com.example.exception.DataNoFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface UserService {
    ResultPaginationResponse getUserPage(Optional<String> current, Optional<String> pageSize);

    ResultPaginationResponse getUserFilter(Specification<User> spec , Pageable pageable);

    UserResponse getUser(Long id) throws DataNoFoundException;

    List<UserResponse> getUsers();

    User getUserByEmail(String email) throws DataNoFoundException;

    UserResponse create(CreateUserRequest request) throws AppException;

    UserResponse update(UpdateUserRequest request) throws DataNoFoundException;

    void deleted(Long id) throws DataNoFoundException;

}
