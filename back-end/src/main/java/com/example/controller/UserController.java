package com.example.controller;

import com.example.dto.request.CreateUserRequest;
import com.example.dto.request.UpdateUserRequest;
import com.example.dto.response.UserResponse;
import com.example.exception.AppException;
import com.example.util.annotation.ApiMessage;
import com.example.dto.response.ResultPaginationResponse;
import com.example.entity.User;
import com.example.exception.DataNoFoundException;
import com.example.service.UserService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @GetMapping("{id}")
    @ApiMessage("Get User By Id")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id) throws DataNoFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(id));
    }

//    @GetMapping
//    public ResponseEntity<ResultPaginationResponse> getUsers(
//            @RequestParam(name = "current", defaultValue = "0") Optional<String> current,
//            @RequestParam(name = "pageSize", defaultValue = "5") Optional<String> pageSize
//    ) {
//        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserPage(current, pageSize));
//    }

    @GetMapping
    @ApiMessage("Get All User Filter")
    public ResponseEntity<ResultPaginationResponse> getUsersFilter(
            @Filter Specification<User> spec,
            Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                userService.getUserFilter(spec, pageable)
        );
    }

    @PostMapping
    @ApiMessage("Created User Success")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) throws AppException {
        UserResponse response = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    @ApiMessage("Updated User Success")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UpdateUserRequest request) throws DataNoFoundException {
        UserResponse response = userService.update(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("{id}")
    @ApiMessage("Delete A User")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws DataNoFoundException {
        userService.deleted(id);
        return ResponseEntity.ok(null);
    }

}

