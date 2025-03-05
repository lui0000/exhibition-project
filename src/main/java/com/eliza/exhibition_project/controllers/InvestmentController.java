package com.eliza.exhibition_project.controllers;

import com.eliza.exhibition_project.dto.InvestmentDto;
import com.eliza.exhibition_project.models.Investment;
import com.eliza.exhibition_project.services.InvestmentService;
import com.eliza.exhibition_project.util.ErrorResponse.InvestmentErrorResponse;
import com.eliza.exhibition_project.util.NotCreatedException.InvestmentNotCreatedException;
import com.eliza.exhibition_project.util.NotFoundException.InvestmentNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("investment")
public class InvestmentController {

    private final InvestmentService investmentService;
    private final ModelMapper modelMapper;


    @Autowired
    public InvestmentController(InvestmentService investmentService, ModelMapper modelMapper) {
        this.investmentService = investmentService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<InvestmentDto> getInvestments() {
        return investmentService.findAll().stream().map(this::convertToInvestmentDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public InvestmentDto getInvestment(@PathVariable("id") int id) {
        return convertToInvestmentDto(investmentService.findOne(id));
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid InvestmentDto investmentDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                stringBuilder.append(error.getField()).append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new InvestmentNotCreatedException(stringBuilder.toString());
        }

        investmentService.save(convertToInvestment(investmentDto));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<InvestmentErrorResponse> handleException(InvestmentNotFoundException e) {
        InvestmentErrorResponse response = new InvestmentErrorResponse(
                "Investment with this id wasn't found", System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<InvestmentErrorResponse> handleException(InvestmentNotCreatedException e) {
        InvestmentErrorResponse response = new InvestmentErrorResponse(
                e.getMessage(), System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private InvestmentDto convertToInvestmentDto(Investment investment) {
        return modelMapper.map(investment, InvestmentDto.class);
    }

    private Investment convertToInvestment(InvestmentDto investmentDto) {
        return modelMapper.map(investmentDto, Investment.class);
    }
}
