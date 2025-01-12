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
import com.citb.app.services.AmazonS3Service;
import com.citb.app.services.PortfolioService;
import com.citb.app.utils.RandomUtil;
import org.springframework.web.multipart.MultipartFile;
@Service
public class PortfolioServiceImpl implements PortfolioService{

	
	@Autowired
	private PortfolioRepo portRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AmazonS3Service amazonS3Service;
	
	@Override
	public PortfolioDTO createPortfolio(PortfolioDTO portfolioDTO, List<MultipartFile> images) {
		
		if (images.size() > 5) {
            throw new IllegalArgumentException("A portfolio cannot have more than 5 images.");
        }
		
		   List<String> imageUrls = images.stream()
	                .map(amazonS3Service::uploadImage)
	                .collect(Collectors.toList());
		   
		   
		Portfolio portfolio = this.modelMapper.map(portfolioDTO, Portfolio.class);
		portfolio.setId(RandomUtil.randomIdentity("Portfolio"));
		portfolio.setImageUrls(imageUrls);
		
		Portfolio createdPortfolio = this.portRepo.save(portfolio);
		
		return this.modelMapper.map(createdPortfolio, PortfolioDTO.class);
	}

	@Override
	public PortfolioDTO updatePortfolio(String portfolioId, PortfolioDTO portfolioDTO, List<MultipartFile> images) {
		
		Portfolio portfolio = this.portRepo.findById(portfolioId)
				.orElseThrow(() -> new ResourceNotFoundException("porrtfolio", "id", portfolioId));
		
	    if(images!=null && !images.isEmpty()) {
			if(images.size()> 5) {
				throw new IllegalArgumentException("A portfolio cannot have more than 5 images.");
			}
			

			   List<String> imageUrls = images.stream()
		                .map(amazonS3Service::uploadImage)
		                .collect(Collectors.toList());
			   
			   portfolio.setImageUrls(imageUrls);
		 }
		
		
		portfolio.setTitle(portfolioDTO.getTitle());
		portfolio.setDescription(portfolioDTO.getDescription());
	
		
		Portfolio updatedPortfolio = this.portRepo.save(portfolio);
		
		return this.modelMapper.map(updatedPortfolio, PortfolioDTO.class);
	}

	@Override
	public PortfolioDTO getPortfolioById(String portfolioId) {

		Portfolio portfolio = this.portRepo.findById(portfolioId).
				orElseThrow(() -> new ResourceNotFoundException("porrtfolio", "id", portfolioId));
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
	public void deletePortfolio(String portfolioId) {
		
		Portfolio portfolio = this.portRepo.findById(portfolioId)
				.orElseThrow(() -> new ResourceNotFoundException("porrtfolio", "id", portfolioId));
		
		List<String> imageUrls = portfolio.getImageUrls();
		
		if(imageUrls!=null || !imageUrls.isEmpty()) {
			imageUrls.forEach(imageUrl ->{
				String imageName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
				amazonS3Service.deleteImage(imageName);
			});
		}
		this.portRepo.delete(portfolio);
	}

}
