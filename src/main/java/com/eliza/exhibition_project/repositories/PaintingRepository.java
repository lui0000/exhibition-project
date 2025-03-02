package com.eliza.exhibition_project.repositories;

import com.eliza.exhibition_project.models.Painting;
import com.eliza.exhibition_project.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaintingRepository  extends JpaRepository<Painting, Integer> {
    Optional<Painting> findByTitle(String title);
}
