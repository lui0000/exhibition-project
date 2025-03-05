package com.eliza.exhibition_project.controllers;

import com.eliza.exhibition_project.dto.PaintingDto;
import com.eliza.exhibition_project.dto.UserDto;
import com.eliza.exhibition_project.models.Painting;
import com.eliza.exhibition_project.models.User;
import com.eliza.exhibition_project.services.PaintingService;
import com.eliza.exhibition_project.services.UserService;
import com.eliza.exhibition_project.util.ErrorResponse.PaintingErrorResponse;
import com.eliza.exhibition_project.util.ErrorResponse.UserErrorResponse;
import com.eliza.exhibition_project.util.NotCreatedException.PaintingNotCreatedException;
import com.eliza.exhibition_project.util.NotCreatedException.UserNotCreatedException;
import com.eliza.exhibition_project.util.NotFoundException.PaintingNotFoundException;
import com.eliza.exhibition_project.util.validator.PaintingValidator;
import com.eliza.exhibition_project.util.validator.UserValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("Painting")
public class PaintingController {
    private final PaintingService paintingService;
    private final ModelMapper modelMapper;
    private final PaintingValidator paintingValidator;


    @Autowired
    public PaintingController(PaintingService paintingService, ModelMapper modelMapper, PaintingValidator paintingValidator) {
        this.paintingService = paintingService;
        this.modelMapper = modelMapper;
        this.paintingValidator = paintingValidator;
    }


    @GetMapping
    public List<PaintingDto> getPaintings() {
        return paintingService.findAll().stream().map(this::convertToPaintingDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PaintingDto getPainting (@PathVariable("id") int id) {
        return convertToPaintingDto(paintingService.findOne(id));
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PaintingDto paintingDto, BindingResult bindingResult) {
        paintingValidator.validate(paintingDto, bindingResult);

        if(bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error: errors) {
                stringBuilder.append(error.getField()).append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw  new PaintingNotCreatedException(stringBuilder.toString());
        }

        paintingService.save(convertToPainting(paintingDto));
        return ResponseEntity.ok(HttpStatus.OK);




    }

    @ExceptionHandler
    private ResponseEntity<PaintingErrorResponse> handleException(PaintingNotFoundException e) {
        PaintingErrorResponse response = new PaintingErrorResponse(
                "Painting with this id wasn't found", System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<PaintingErrorResponse> handleException(PaintingNotCreatedException e) {
        PaintingErrorResponse response = new PaintingErrorResponse(
                e.getMessage(), System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private PaintingDto convertToPaintingDto(Painting painting) {
        return modelMapper.map(painting, PaintingDto.class);
    }

    private Painting convertToPainting(PaintingDto paintingDto) {
        return modelMapper.map(paintingDto, Painting.class);
    }
}
