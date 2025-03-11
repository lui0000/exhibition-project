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
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;

    @Autowired
    public RegisterController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService, ModelMapper modelMapper, UserValidator userValidator) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
    }

    @PostMapping("/register")
    public ResponseEntity<?> performRegister(@RequestBody @Valid UserDto userDto, BindingResult bindingResult) {

        userValidator.validate(userDto, bindingResult);

        if(bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error: errors) {
                stringBuilder.append(error.getField()).append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw  new UserNotCreatedException(stringBuilder.toString());
        }


        User user = convertToUser(userDto);
        userService.registerUser(user.getName(), user.getEmail(), user.getPasswordHash(), user.getRole());
        String token = jwtUtil.generateToken(userDto.getName(), userDto.getRole());
        return ResponseEntity.ok(Map.of("jwt-token", token));
    }


    @PostMapping("/login")
    public ResponseEntity<?> performLogin(@RequestBody LoginUserDto loginUserDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserDto.getName(), loginUserDto.getPasswordHash())
        );

        String username = authentication.getName();
        Role role = ((User) authentication.getPrincipal()).getRole();
        String token = jwtUtil.generateToken(username, role);

        return ResponseEntity.ok(Map.of("jwt-token", token));
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


