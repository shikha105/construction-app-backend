package com.citb.app.services;

import java.util.List;

import com.citb.app.payloads.PortfolioDTO;

import org.springframework.web.multipart.MultipartFile;
public interface PortfolioService {

	 PortfolioDTO createPortfolio(PortfolioDTO portfolioDTO, List<MultipartFile> images);
	 PortfolioDTO updatePortfolio(String portfolioId, PortfolioDTO portfolioDTO, List<MultipartFile> images);
	 PortfolioDTO getPortfolioById( String portfolioId);
	 List<PortfolioDTO> getAllPortfolios();
	 void deletePortfolio(String portfolioId);
}
