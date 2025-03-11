package com.eliza.exhibition_project.controllers;


import com.eliza.exhibition_project.config.JwtUtil;
import com.eliza.exhibition_project.dto.LoginUserDto;
import com.eliza.exhibition_project.dto.UserDto;
import com.eliza.exhibition_project.models.Role;
import com.eliza.exhibition_project.models.User;
import com.eliza.exhibition_project.services.UserService;
import com.eliza.exhibition_project.util.ErrorResponse.UserErrorResponse;
import com.eliza.exhibition_project.util.NotCreatedException.UserNotCreatedException;
import com.eliza.exhibition_project.util.validator.UserValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class RegisterController {
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;

    @Autowired
    public RegisterController(JwtUtil jwtUtil, UserService userService, ModelMapper modelMapper, UserValidator userValidator) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
    }

    @PostMapping("/register")
    public ResponseEntity<?> performRegister(@RequestBody @Valid UserDto userDto, BindingResult bindingResult) {
        userValidator.validate(userDto, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Validation failed"));
        }

        User user = convertToUser(userDto);
        userService.registerUser(user.getName(), user.getEmail(), user.getPasswordHash(), user.getRole());
        String token = jwtUtil.generateToken(userDto.getEmail(), userDto.getRole()); // Теперь передаем email

        return ResponseEntity.ok(Map.of("jwt-token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> performLogin(@RequestBody @Valid LoginUserDto loginUserDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Validation failed"));
        }

        try {
            User user = userService.login(loginUserDto.getEmail(), loginUserDto.getPasswordHash());
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
            return ResponseEntity.ok(Map.of("jwt-token", token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }





    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UsernameNotFoundException e) {
        UserErrorResponse response = new UserErrorResponse(
                "User with this id wasn't found", System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotCreatedException e) {
        UserErrorResponse response = new UserErrorResponse(
                e.getMessage(), System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    public User convertToUser(UserDto userDto) {
        return this.modelMapper.map(userDto, User.class);
    }

}


