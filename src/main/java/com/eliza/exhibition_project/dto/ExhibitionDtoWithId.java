package com.eliza.exhibition_project.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ExhibitionDtoWithId {

    private int id;

    @NotEmpty(message = "Title should not be empty")
    private String title;

    @NotEmpty(message = "Description should not be empty")
    private String description;

    @NotNull(message = "Start date should not be empty")
    @Temporal(TemporalType.DATE)
    private LocalDate startDate;

    @NotNull(message = "End date should not be empty")
    @Temporal(TemporalType.DATE)
    private LocalDate endDate;

    private UserDto organizer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @NotEmpty(message = "Title should not be empty") String getTitle() {
        return title;
    }

    public void setTitle(@NotEmpty(message = "Title should not be empty") String title) {
        this.title = title;
    }

    public @NotEmpty(message = "Description should not be empty") String getDescription() {
        return description;
    }

    public void setDescription(@NotEmpty(message = "Description should not be empty") String description) {
        this.description = description;
    }

    public @NotNull(message = "Start date should not be empty") LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(@NotNull(message = "Start date should not be empty") LocalDate startDate) {
        this.startDate = startDate;
    }

    public @NotNull(message = "End date should not be empty") LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(@NotNull(message = "End date should not be empty") LocalDate endDate) {
        this.endDate = endDate;
    }

    public UserDto getOrganizer() {
        return organizer;
    }

    public void setOrganizer(UserDto organizer) {
        this.organizer = organizer;
    }
}
