package com.eliza.exhibition_project.controllers;

import com.eliza.exhibition_project.dto.UserDto;
import com.eliza.exhibition_project.models.User;
import com.eliza.exhibition_project.services.UserService;
import com.eliza.exhibition_project.util.ErrorResponse.UserErrorResponse;
import com.eliza.exhibition_project.util.NotCreatedException.UserNotCreatedException;
import com.eliza.exhibition_project.util.validator.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper, UserValidator userValidator) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
    }

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.findAll().stream().map(this::convertToUserDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto getUser (@PathVariable("id") int id) {
        return convertToUserDto(userService.findOne(id));
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid UserDto userDto, BindingResult bindingResult) {
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

        userService.save(convertToUser(userDto));
        return ResponseEntity.ok(HttpStatus.OK);




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

    private UserDto convertToUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private User convertToUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
}
