package com.eliza.exhibition_project.controllers;


import com.eliza.exhibition_project.config.JwtUtil;
import com.eliza.exhibition_project.dto.LoginUserDto;
import com.eliza.exhibition_project.dto.UserRegisterDto;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

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
    public ResponseEntity<?> performRegister(@RequestBody @Valid UserRegisterDto userDto, BindingResult bindingResult) {
        userValidator.validate(userDto, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Validation failed"));
        }

        User user = convertToUser(userDto);
        userService.registerUser(user.getName(), user.getEmail(), user.getPasswordHash(), user.getRole());

        Optional<User> savedUserOptional = userService.findByEmail(user.getEmail());

        User savedUser = savedUserOptional.orElseThrow(() ->
                new UsernameNotFoundException("User not found after registration"));

        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRole(), savedUser.getId());


        return ResponseEntity.ok(Map.of("jwtToken", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> performLogin(@RequestBody @Valid LoginUserDto loginUserDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Validation failed"));
        }

        try {
            User user = userService.login(loginUserDto.getEmail(), loginUserDto.getPasswordHash());
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getId());
            return ResponseEntity.ok(Map.of("jwtToken", token));
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
    public User convertToUser(UserRegisterDto userDto) {
        return this.modelMapper.map(userDto, User.class);
    }

}


