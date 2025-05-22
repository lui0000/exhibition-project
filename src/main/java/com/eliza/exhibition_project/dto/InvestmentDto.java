package com.eliza.exhibition_project.dto;

import com.eliza.exhibition_project.models.Exhibition;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class InvestmentDto {

    private UserDto investor;
    private Exhibition exhibition;

    @NotNull(message = "Amount should not be null")
    @Positive(message = "Amount should be a positive number")
    private double amount;

    public UserDto getInvestor() {
        return investor;
    }

    public void setInvestor(UserDto investor) {
        this.investor = investor;
    }

    public Exhibition getExhibition() {
        return exhibition;
    }

    public void setExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
