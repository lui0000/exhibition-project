package com.eliza.exhibition_project.repositories;

import com.eliza.exhibition_project.models.Exhibition;
import com.eliza.exhibition_project.models.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Integer> {
    List<Investment> findByExhibition(Exhibition exhibition);
}