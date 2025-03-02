package com.eliza.exhibition_project.services;

import com.eliza.exhibition_project.models.Painting;
import com.eliza.exhibition_project.models.User;
import com.eliza.exhibition_project.repositories.PaintingRepository;
import com.eliza.exhibition_project.util.PaintingNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PaintingService {

    private final PaintingRepository paintingRepository;
    private final UserService userService;

    public PaintingService(PaintingRepository paintingRepository, UserService userService) {
        this.paintingRepository = paintingRepository;
        this.userService = userService;
    }

    public List<Painting> findAll() {
        return paintingRepository.findAll();
    }

    public Painting findOne(int id){
        Optional<Painting> foundPainting = paintingRepository.findById(id);

        return foundPainting.orElseThrow(PaintingNotFoundException::new);
    }
    @Transactional
    public void save(Painting painting) {
        enrichPainting(painting);
        paintingRepository.save(painting);
    }

    public void enrichPainting(Painting painting) {
        Optional<User> artistOptional = userService.findByName(painting.getArtist().getName());
        artistOptional.ifPresent(painting::setArtist);

    }


    public Optional<Painting> findByTitle(String title) {
        Optional<Painting> foundPainting = paintingRepository.findByTitle(title);
        return Optional.ofNullable(foundPainting.orElseThrow(PaintingNotFoundException::new));
    }
}
