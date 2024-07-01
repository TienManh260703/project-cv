package com.example.service.impl;

import com.example.dto.response.Meta;
import com.example.dto.response.ResultPaginationResponse;
import com.example.entity.User;
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
    public User getUser(Long id) throws DataNoFoundException {
        return userRepository.findById(id).orElseThrow(
                () -> new DataNoFoundException("Data not found"));
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
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
