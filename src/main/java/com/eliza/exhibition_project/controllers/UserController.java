package com.eliza.exhibition_project.controllers;

import com.eliza.exhibition_project.dto.UserDto;
import com.eliza.exhibition_project.services.UserService;
import com.eliza.exhibition_project.util.validator.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
        return convertToSensorDTO(userService.findOne(id));
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid UserDto, BindingResult bindingResult) {

    }
}
