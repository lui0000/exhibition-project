package com.eliza.exhibition_project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Objects;

@Entity
@Table(name = "investments")
public class Investment {

    @Id
    @Column(name = "investment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Investor should not be null")
    @ManyToOne
    @JoinColumn(name = "investor_id", referencedColumnName = "user_id")
    private User investor;

    @NotNull(message = "Exhibition should not be null")
    @ManyToOne
    @JoinColumn(name = "exhibition_id", referencedColumnName = "exhibition_id")
    private Exhibition exhibition;

    @NotNull(message = "Amount should not be null")
    @Positive(message = "Amount should be a positive number")
    @Column(name = "amount", nullable = false)
    private double amount;




    public Investment() {
    }

//    public Investment(User investor, Exhibition exhibition, double amount) {
//        this.investor = investor;
//        this.exhibition = exhibition;
//        this.amount = amount;
//    }

    // Геттеры и сеттеры

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    // equals, hashCode и toString

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Investment that = (Investment) o;
        return id == that.id && Double.compare(that.amount, amount) == 0 && Objects.equals(investor, that.investor) && Objects.equals(exhibition, that.exhibition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, investor, exhibition, amount);
    }

    @Override
    public String toString() {
        return "Investment{" +
                "id=" + id +
                ", investor=" + investor +
                ", exhibition=" + exhibition +
                ", amount=" + amount +
                '}';
    }
}