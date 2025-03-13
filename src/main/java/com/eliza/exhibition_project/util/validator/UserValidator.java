package com.eliza.exhibition_project.util.validator;

import com.eliza.exhibition_project.dto.UserRegisterDto;
import com.eliza.exhibition_project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserRegisterDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRegisterDto userDto = (UserRegisterDto) target;
        if(userService.findByName(userDto.getName()).isPresent())
            errors.rejectValue("name", "", "User with this name already exists");
    }


}
