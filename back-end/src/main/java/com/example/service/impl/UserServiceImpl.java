package com.example.service.impl;

import com.example.entity.User;
import com.example.exception.DataNoFoundException;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public User getUser(Long id) throws DataNoFoundException {
        return userRepository.findById(id).orElseThrow(
                () -> new DataNoFoundException("Data not found"));
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email) throws DataNoFoundException {
        User existingUser = userRepository.findByEmail(email).orElseThrow(() -> (
                new DataNoFoundException("Cannot find user with email : " + email)
        ));
        return existingUser;
    }

    @Override
    public User create(User request) {
        String hashPassword = this.passwordEncoder.encode(request.getPassword());
        request.setPassword(hashPassword);
        return userRepository.save(request);
    }

    @Override
    public User update(User request) throws DataNoFoundException {
        String hashPassword = this.passwordEncoder.encode(request.getPassword());
        User existingUser = getUser(request.getId());
        existingUser.setEmail(request.getEmail());
        existingUser.setName(request.getName());
        existingUser.setPassword(hashPassword);
        User response = userRepository.save(existingUser);
        return response;
    }

    @Override
    public void deleted(Long id) {
        userRepository.deleteById(id);
    }
}
