package com.eliza.exhibition_project.services;


import com.eliza.exhibition_project.dto.ExhibitionDto;
import com.eliza.exhibition_project.dto.ExhibitionWithAdditionalInfoDto;
import com.eliza.exhibition_project.dto.ExhibitionWithPaintingDTO;
import com.eliza.exhibition_project.models.*;
import com.eliza.exhibition_project.repositories.ExhibitionRepository;
import com.eliza.exhibition_project.repositories.InvestmentRepository;
import com.eliza.exhibition_project.repositories.PaintingRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eliza.exhibition_project.util.NotFoundException.ExhibitionNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ExhibitionService {
    private final ExhibitionRepository exhibitionRepository;
    private final UserService userService;
    private final PaintingRepository paintingRepository;
    private final InvestmentRepository investmentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    private HttpServletRequest request;


    public ExhibitionService(ExhibitionRepository exhibitionRepository, UserService userService, PaintingRepository paintingRepository, InvestmentRepository investmentRepository, ModelMapper modelMapper) {
        this.exhibitionRepository = exhibitionRepository;
        this.userService = userService;
        this.paintingRepository = paintingRepository;
        this.investmentRepository = investmentRepository;
        this.modelMapper = modelMapper;
    }


    public List<ExhibitionWithPaintingDTO> getExhibitionsWithApprovedImage() {
        List<Exhibition> exhibitions = exhibitionRepository.findAll();
        List<ExhibitionWithPaintingDTO> exhibitionDTOs = new ArrayList<>();

        for (Exhibition exhibition : exhibitions) {
            Optional<Painting> approvedPainting = paintingRepository
                    .findFirstByExhibitionIdAndStatusOrderByIdAsc(exhibition.getId(), PaintingStatus.APPROVED);


            String imageUrl = approvedPainting.map(Painting::getPhotoData).orElse(null);

            exhibitionDTOs.add(new ExhibitionWithPaintingDTO(
                    exhibition.getTitle(),
                    exhibition.getDescription(),
                    imageUrl
            ));
        }

        return exhibitionDTOs;
    }

    public ExhibitionWithAdditionalInfoDto getExhibitionsWithAdditionalInfo(String name) {
        Exhibition exhibition = findByTitle(name)
                .orElseThrow(ExhibitionNotFoundException::new);

        List<Painting> approvedPaintings = paintingRepository.findByExhibitionAndStatus(exhibition, PaintingStatus.APPROVED);
        List<Investment> investments = investmentRepository.findByExhibition(exhibition);

        List<String> artists = approvedPaintings.stream()
                .map(painting -> painting.getArtist().getName())
                .distinct()
                .collect(Collectors.toList());

        List<String> investors = investments.stream()
                .map(investment -> investment.getInvestor().getName())
                .distinct()
                .collect(Collectors.toList());

        List<String> paintingImages = approvedPaintings.stream()
                .map(Painting::getPhotoData)
                .collect(Collectors.toList());

        String exhibitionPhoto = paintingImages.stream()
                .findFirst()
                .orElse(null);

        ExhibitionDto exhibitionDto = modelMapper.map(exhibition, ExhibitionDto.class);

        return new ExhibitionWithAdditionalInfoDto(
                exhibitionPhoto,
                exhibitionDto,
                paintingImages,
                artists,
                investors
        );
    }




    public Exhibition findOne(int id){
        Optional<Exhibition> foundExhibition = exhibitionRepository.findById(id);
        return foundExhibition.orElseThrow(ExhibitionNotFoundException::new);
    }



    @Transactional
    public void save(Exhibition exhibition) {
        Integer organizerId = (Integer) request.getAttribute("user_id");
        if (organizerId == null) {
            throw new RuntimeException("Organizer ID not found in request");
        }

        User organizer = userService.findById(organizerId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + organizerId));

        exhibition.setOrganizer(organizer);
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
