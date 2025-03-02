package com.eliza.exhibition_project.dto;

import com.eliza.exhibition_project.models.Exhibition;
import com.eliza.exhibition_project.models.User;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotEmpty;

public class PaintingDto {

    @NotEmpty(message = "Title should not be empty")
    private String title;


    private String style;
    private String description;
    private UserDto artist;
    private ExhibitionDto exhibition;
    @Lob
    private byte[] photoData;

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

    public ExhibitionDto getExhibition() {
        return exhibition;
    }

    public void setExhibition(ExhibitionDto exhibition) {
        this.exhibition = exhibition;
    }

    public byte[] getPhotoData() {
        return photoData;
    }

    public void setPhotoData(byte[] photoData) {
        this.photoData = photoData;
    }
}
