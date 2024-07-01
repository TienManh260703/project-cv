package com.example.service;

import com.example.dto.response.ResultPaginationResponse;
import com.example.entity.User;
import com.example.exception.DataNoFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface UserService {
    ResultPaginationResponse getUserPage(Optional<String> current, Optional<String> pageSize);

    ResultPaginationResponse getUserFilter(Specification<User> spec , Pageable pageable);

    User getUser(Long id) throws DataNoFoundException;

    List<User> getUsers();

    User getUserByEmail(String email) throws DataNoFoundException;

    User create(User request);

    User update(User request) throws DataNoFoundException;

    void deleted(Long id);

}
