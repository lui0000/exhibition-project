package com.eliza.exhibition_project.dto;

import com.eliza.exhibition_project.models.Exhibition;
import com.eliza.exhibition_project.models.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class InvestmentDto {

    private User investor;
    private Exhibition exhibition;

    @NotNull(message = "Amount should not be null")
    @Positive(message = "Amount should be a positive number")
    private double amount;

    public User getInvestor() {
        return investor;
    }

    public void setInvestor(User investor) {
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
