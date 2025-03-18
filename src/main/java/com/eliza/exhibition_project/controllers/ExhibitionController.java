package com.eliza.exhibition_project.controllers;

import com.eliza.exhibition_project.dto.ExhibitionDto;
import com.eliza.exhibition_project.dto.ExhibitionWithPaintingDTO;
import com.eliza.exhibition_project.models.Exhibition;
import com.eliza.exhibition_project.services.ExhibitionService;
import com.eliza.exhibition_project.util.ErrorResponse.ExhibitionErrorResponse;
import com.eliza.exhibition_project.util.NotCreatedException.ExhibitionNotCreatedException;
import com.eliza.exhibition_project.util.NotFoundException.ExhibitionNotFoundException;
import com.eliza.exhibition_project.util.validator.ExhibitionValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("exhibition")
public class ExhibitionController {

    private final ExhibitionService exhibitionService;
    private final ModelMapper modelMapper;
    private final ExhibitionValidator exhibitionValidator;

    @Autowired
    public ExhibitionController(ExhibitionService exhibitionService, ModelMapper modelMapper, ExhibitionValidator exhibitionValidator) {
        this.exhibitionService = exhibitionService;
        this.modelMapper = modelMapper;
        this.exhibitionValidator = exhibitionValidator;
    }

    @GetMapping
    public List<ExhibitionWithPaintingDTO> getExhibitions() {
        return new ArrayList<>(exhibitionService.getExhibitionsWithApprovedImage());
    }

    @GetMapping("/{id}")
    public ExhibitionDto getExhibition(@PathVariable("id") int id) {
        return convertToExhibitionDto(exhibitionService.findOne(id));
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid ExhibitionDto exhibitionDto, BindingResult bindingResult) {
        exhibitionValidator.validate(exhibitionDto, bindingResult);

        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                stringBuilder.append(error.getField()).append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new ExhibitionNotCreatedException(stringBuilder.toString());
        }

        exhibitionService.save(convertToExhibition(exhibitionDto));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ExhibitionErrorResponse> handleException(ExhibitionNotFoundException e) {
        ExhibitionErrorResponse response = new ExhibitionErrorResponse(
                "Exhibition with this id wasn't found", System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ExhibitionErrorResponse> handleException(ExhibitionNotCreatedException e) {
        ExhibitionErrorResponse response = new ExhibitionErrorResponse(
                e.getMessage(), System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ExhibitionDto convertToExhibitionDto(Exhibition exhibition) {
        return modelMapper.map(exhibition, ExhibitionDto.class);
    }

    private Exhibition convertToExhibition(ExhibitionDto exhibitionDto) {
        return modelMapper.map(exhibitionDto, Exhibition.class);
    }
}

