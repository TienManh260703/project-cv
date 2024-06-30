package com.example.service;

import com.example.dto.response.ResultPaginationResponse;
import com.example.entity.User;
import com.example.exception.DataNoFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    ResultPaginationResponse getUserPage(Optional<String> current , Optional<String> pageSize);
    User getUser (Long id) throws DataNoFoundException;
    List<User> getUsers ();
    User getUserByEmail (String email) throws DataNoFoundException;
    User create (User request);
    User update(User request) throws DataNoFoundException;
    void  deleted(Long id);

}
