package com.eliza.exhibition_project.dto;

import com.eliza.exhibition_project.models.Exhibition;

import java.util.List;
import java.util.Optional;

public class ExhibitionWithAdditionalInfoDto {
    private String photoData;
    private ExhibitionDto exhibitionDto;
    private List<String> paintingImages;
    private List<String> artists;
    private List<String> investors;

    public ExhibitionWithAdditionalInfoDto() {
    }

    public ExhibitionWithAdditionalInfoDto(String photoData, ExhibitionDto exhibitionDto,
                                           List<String> paintingImages, List<String> artists, List<String> investors) {
        this.photoData = photoData;
        this.exhibitionDto = exhibitionDto;
        this.paintingImages = paintingImages;
        this.artists = artists;
        this.investors = investors;
    }

    // Геттеры и сеттеры
    public String getPhotoData() {
        return photoData;
    }

    public void setPhotoData(String photoData) {
        this.photoData = photoData;
    }

    public ExhibitionDto getExhibitionDto() {
        return exhibitionDto;
    }

    public void setExhibitionDto(ExhibitionDto exhibitionDto) {
        this.exhibitionDto = exhibitionDto;
    }

    public List<String> getPaintingImages() {
        return paintingImages;
    }

    public void setPaintingImages(List<String> paintingImages) {
        this.paintingImages = paintingImages;
    }

    public List<String> getArtists() {
        return artists;
    }

    public void setArtists(List<String> artists) {
        this.artists = artists;
    }

    public List<String> getInvestors() {
        return investors;
    }

    public void setInvestors(List<String> investors) {
        this.investors = investors;
    }
}