package com.eliza.exhibition_project.services;

import com.eliza.exhibition_project.models.Investment;
import com.eliza.exhibition_project.models.User;
import com.eliza.exhibition_project.repositories.InvestmentRepository;
import com.eliza.exhibition_project.util.NotFoundException.PaintingNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class InvestmentService {

    private final InvestmentRepository investmentRepository;
    private final UserService userService;

    public InvestmentService(InvestmentRepository investmentRepository, UserService userService) {
        this.investmentRepository = investmentRepository;
        this.userService = userService;
    }

    public List<Investment> findAll() {
        return investmentRepository.findAll();
    }

    public Investment findOne(int id){
        Optional<Investment> foundInvestment = investmentRepository.findById(id);

        return foundInvestment.orElseThrow(PaintingNotFoundException::new);
    }
    @Transactional
    public void save(Investment investment) {
        enrichInvestment(investment);
        investmentRepository.save(investment);
    }

    public void enrichInvestment(Investment investment) {
        Optional<User> investmentOptional = userService.findByName(investment.getInvestor().getName());
        investmentOptional.ifPresent(investment::setInvestor);
    }
}
