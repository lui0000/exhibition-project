package com.eliza.exhibition_project.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    INVESTOR,  // Инвестор
    ORGANIZER, // Организатор
    ARTIST;

    @JsonCreator
    public static Role fromString(String value) {
        return Role.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toString() {
        return this.name();
    }// Художник
}
