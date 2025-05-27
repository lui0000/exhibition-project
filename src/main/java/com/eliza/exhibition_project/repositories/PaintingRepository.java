package com.eliza.exhibition_project.repositories;

import com.eliza.exhibition_project.models.Exhibition;
import com.eliza.exhibition_project.models.Painting;
import com.eliza.exhibition_project.models.PaintingStatus;
import com.eliza.exhibition_project.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaintingRepository  extends JpaRepository<Painting, Integer> {
    Optional<Painting> findByTitle(String title);
    Optional<Painting> findByPhotoData(String painting);

    Optional<Painting> findFirstByExhibitionIdAndStatusOrderByIdAsc(int exhibitionId, PaintingStatus status);

    List<Painting> findByExhibitionAndStatus(Exhibition exhibition, PaintingStatus status);
}
