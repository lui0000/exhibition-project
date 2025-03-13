package com.eliza.exhibition_project.util.validator;

import com.eliza.exhibition_project.dto.ExhibitionDto;
import com.eliza.exhibition_project.services.ExhibitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ExhibitionValidator implements Validator {

    private final ExhibitionService exhibitionService;

    @Autowired
    public ExhibitionValidator(ExhibitionService exhibitionService) {
        this.exhibitionService = exhibitionService;
    }




    @Override
    public boolean supports(Class<?> clazz) {
        return ExhibitionDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ExhibitionDto exhibitionDto = (ExhibitionDto) target;
        if(exhibitionService.findByTitle(exhibitionDto.getTitle()).isPresent())
            errors.rejectValue("title", "", "Exhibition with this title already exists");
    }

}

