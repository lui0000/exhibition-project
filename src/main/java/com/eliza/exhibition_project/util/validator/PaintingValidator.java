package com.eliza.exhibition_project.util.validator;

import com.eliza.exhibition_project.dto.PaintingDto;
import com.eliza.exhibition_project.dto.UserDto;
import com.eliza.exhibition_project.services.PaintingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PaintingValidator implements Validator {

    private final PaintingService paintingService;

    @Autowired
    public PaintingValidator(PaintingService paintingService) {
        this.paintingService = paintingService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return PaintingDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PaintingDto paintingDto = (PaintingDto) target;
        if(paintingService.findByTitle(paintingDto.getTitle()).isPresent())
            errors.rejectValue("title", "", "Painting with this title already exists");
    }
}
