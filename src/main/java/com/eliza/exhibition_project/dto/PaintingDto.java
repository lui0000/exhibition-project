package com.eliza.exhibition_project.dto;

import com.eliza.exhibition_project.models.Exhibition;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotEmpty;

public class PaintingDto {

    @NotEmpty(message = "Title should not be empty")
    private String title;


    private String style;
    private String description;
    private UserDto artist;
    private Exhibition exhibition;
    private String photoData;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserDto getArtist() {
        return artist;
    }

    public void setArtist(UserDto artist) {
        this.artist = artist;
    }

    public Exhibition getExhibition() {
        return exhibition;
    }

    public void setExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
    }

    public String getPhotoData() {
        return photoData;
    }

    public void setPhotoData(String photoData) {
        this.photoData = photoData;
    }
}
