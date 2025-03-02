package com.eliza.exhibition_project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "paintings")
public class Painting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "painting_id")
    private int id;

    @NotEmpty(message = "Title should not be empty")
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "style")
    private String style;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "artist_id", referencedColumnName = "user_id", nullable = false)
    private User artist;

    @ManyToOne
    @JoinColumn(name = "exhibition_id", referencedColumnName = "exhibition_id", nullable = false)
    private Exhibition exhibition;

    @Lob
    @Column(name = "image_url", nullable = false)
    private byte[] photoData;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaintingStatus status = PaintingStatus.PENDING;

    public Painting() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public User getArtist() {
        return artist;
    }

    public void setArtist(User artist) {
        this.artist = artist;
    }

    public Exhibition getExhibition() {
        return exhibition;
    }

    public void setExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
    }

    public byte[] getPhotoData() {
        return photoData;
    }

    public void setPhotoData(byte[] photoData) {
        this.photoData = photoData;
    }

    public PaintingStatus getStatus() {
        return status;
    }

    public void setStatus(PaintingStatus status) {
        this.status = status;
    }
}
