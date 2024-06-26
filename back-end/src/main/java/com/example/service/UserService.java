package com.example.service;

import com.example.entity.User;
import com.example.exception.DataNoFoundException;

import java.util.List;

public interface UserService {

    User getUser (Long id) throws DataNoFoundException;
    List<User> getUsers ();
    User getUserByEmail (String email) throws DataNoFoundException;
    User create (User request);
    User update(User request) throws DataNoFoundException;
    void  deleted(Long id);

}
