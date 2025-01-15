package com.citb.app.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.citb.app.entities.Portfolio;
import com.citb.app.entities.Role;
import com.citb.app.entities.User;
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
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	public PortfolioDTO createPortfolio(PortfolioDTO portfolioDTO, List<MultipartFile> images) {
		if (images.size() > 5) {
            throw new IllegalArgumentException("A portfolio cannot have more than 5 images.");
        }
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();
		User userInfo = (User) userDetailsService.loadUserByUsername(username);

		
		   List<String> imageKeys = images.stream()
	                .map(amazonS3Service::uploadImage)
	                .collect(Collectors.toList());
		   
		 
		Portfolio portfolio = this.modelMapper.map(portfolioDTO, Portfolio.class);
		portfolio.setId(RandomUtil.randomIdentity("Portfolio"));
		portfolio.setImageUrls(imageKeys);
		portfolio.setUser(userInfo);
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
			

			   List<String> imageKeys = images.stream()
		                .map(amazonS3Service::uploadImage)
		                .collect(Collectors.toList());
			   
			   portfolio.setImageUrls(imageKeys);
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
		
		List<String> preSignedUrls = portfolio.getImageUrls().stream()
				.map(amazonS3Service::preSignedUrl)
				.collect(Collectors.toList());
		portfolio.setImageUrls(preSignedUrls);
		return this.modelMapper.map(portfolio, PortfolioDTO.class);
	}

	@Override
	public List<PortfolioDTO> getAllPortfolios() {

        List<Portfolio> portfolios = this.portRepo.findAll();
 
        
       return  portfolios.stream()
        		.map(portfolio ->{
        			PortfolioDTO portfolioDTO = this.modelMapper.map(portfolio, PortfolioDTO.class);
        			
        			List<String> preSignedUrls = portfolio.getImageUrls().stream()
        					.map(amazonS3Service::preSignedUrl)
        					.collect(Collectors.toList());
        			portfolioDTO.setImageUrls(preSignedUrls);
        			return portfolioDTO;
        			
        		}).collect(Collectors.toList());
	}

	@Override
	public void deletePortfolio(String portfolioId) {
		
		Portfolio portfolio = this.portRepo.findById(portfolioId)
				.orElseThrow(() -> new ResourceNotFoundException("porrtfolio", "id", portfolioId));
		
		List<String> imageKeys = portfolio.getImageUrls();
		
		if(imageKeys!=null && !imageKeys.isEmpty()) {
			    imageKeys.forEach(imageKey ->{
				
				amazonS3Service.deleteImage(imageKey);
			});
		}
		this.portRepo.delete(portfolio);
	}

	@Override
	public List<PortfolioDTO> getAllPortfoliosByUserId(String userId) {
		 List<Portfolio> portfolios = this.portRepo.findByUserId(userId);
		 
		 
			return portfolios.stream().map(portfolio -> {
				PortfolioDTO portfolioDTO = this.modelMapper.map(portfolio, PortfolioDTO.class);

				List<String> preSignedUrls = portfolio.getImageUrls().stream().map(amazonS3Service::preSignedUrl)
						.collect(Collectors.toList());
				portfolioDTO.setImageUrls(preSignedUrls);
				return portfolioDTO;

			}).collect(Collectors.toList());
	}

}
