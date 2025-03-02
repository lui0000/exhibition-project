package com.eliza.exhibition_project.repositories;

import com.eliza.exhibition_project.models.Exhibition;
import com.eliza.exhibition_project.models.Painting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, Integer> {
    Exhibition findByTitle(String title);
}
