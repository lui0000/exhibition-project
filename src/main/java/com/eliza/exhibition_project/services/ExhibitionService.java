package com.eliza.exhibition_project.services;


import com.eliza.exhibition_project.models.Exhibition;
import com.eliza.exhibition_project.models.User;
import com.eliza.exhibition_project.repositories.ExhibitionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eliza.exhibition_project.util.NotFoundException.ExhibitionNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ExhibitionService {
    private final ExhibitionRepository exhibitionRepository;
    private final UserService userService;

    public ExhibitionService(ExhibitionRepository exhibitionRepository, UserService userService) {
        this.exhibitionRepository = exhibitionRepository;
        this.userService = userService;
    }

    public List<Exhibition> findAll() {
        return exhibitionRepository.findAll();
    }

    public Exhibition findOne(int id){
        Optional<Exhibition> foundExhibition = exhibitionRepository.findById(id);

        return foundExhibition.orElseThrow(ExhibitionNotFoundException::new);
    }
    @Transactional
    public void save(Exhibition exhibition) {
        enrichExhibition(exhibition);
        exhibitionRepository.save(exhibition);

    }

    public void enrichExhibition(Exhibition exhibition) {
        Optional<User> exhibitionOptional = userService.findByName(exhibition.getOrganizer().getName());
        exhibitionOptional.ifPresent(exhibition::setOrganizer);
    }

    public Optional<Exhibition> findByTitle(String title) {
        Optional<Exhibition> foundExhibition = Optional.ofNullable(exhibitionRepository.findByTitle(title));
        return Optional.ofNullable(foundExhibition.orElseThrow(ExhibitionNotFoundException::new));
    }
}
