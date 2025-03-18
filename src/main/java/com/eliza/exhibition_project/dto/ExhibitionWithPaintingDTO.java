package com.eliza.exhibition_project.dto;

public class ExhibitionWithPaintingDTO {
    private String title;
    private String description;
    private String photoData;

    public ExhibitionWithPaintingDTO(String title, String description, String photoBase64) {
        this.title = title;
        this.description = description;
        this.photoData = photoBase64;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoData() {
        return photoData;
    }

    public void setPhotoData(String photoData) {
        this.photoData = photoData;
    }
}
