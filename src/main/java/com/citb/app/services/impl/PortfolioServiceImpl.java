package com.citb.app.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.citb.app.entities.Portfolio;
import com.citb.app.exceptions.ResourceNotFoundException;
import com.citb.app.payloads.PortfolioDTO;
import com.citb.app.repositories.PortfolioRepo;
import com.citb.app.services.PortfolioService;

@Service
public class PortfolioServiceImpl implements PortfolioService{

	
	@Autowired
	private PortfolioRepo portRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public PortfolioDTO createPortfolio(PortfolioDTO portfolioDTO) {
		
		Portfolio portfolio = this.modelMapper.map(portfolioDTO, Portfolio.class);
		Portfolio createdPortfolio = this.portRepo.save(portfolio);
		
		
		return this.modelMapper.map(createdPortfolio, PortfolioDTO.class);
	}

	@Override
	public PortfolioDTO updatePortfolio(PortfolioDTO portfolioDTO, Integer portfolioId) {
		
		Portfolio portfolio = this.portRepo.findById(portfolioId).orElseThrow(() -> new ResourceNotFoundException("porrtfolio", "id", portfolioId));
		
		portfolio.setTitle(portfolioDTO.getTitle());
		portfolio.setDescription(portfolioDTO.getDescription());
		
		Portfolio updatedPortfolio = this.portRepo.save(portfolio);
		
		return this.modelMapper.map(updatedPortfolio, PortfolioDTO.class);
	}

	@Override
	public PortfolioDTO getPortfoliobyId(Integer portfolioId) {

		Portfolio portfolio = this.portRepo.findById(portfolioId).orElseThrow(() -> new ResourceNotFoundException("porrtfolio", "id", portfolioId));
		return this.modelMapper.map(portfolio, PortfolioDTO.class);
	}

	@Override
	public List<PortfolioDTO> getAllPortfolios() {

        List<Portfolio> portfolios = this.portRepo.findAll();
        List<PortfolioDTO> portfolioDTOs = portfolios.stream()
        		.map(portfolio -> this.modelMapper.map(portfolio, PortfolioDTO.class))
        		.collect(Collectors.toList());
		
		return portfolioDTOs;
	}

	@Override
	public void deletePortfolio(Integer portfolioId) {
		
		Portfolio portfolio = this.portRepo.findById(portfolioId).orElseThrow(() -> new ResourceNotFoundException("porrtfolio", "id", portfolioId));
		this.portRepo.delete(portfolio);
	}

}
