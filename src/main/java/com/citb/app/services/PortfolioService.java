package com.citb.app.services;

import java.util.List;

import com.citb.app.payloads.PortfolioDTO;

public interface PortfolioService {

	 PortfolioDTO createPortfolio(PortfolioDTO portfolioDTO);
	 PortfolioDTO updatePortfolio(PortfolioDTO portfolioDTO, String portfolioId);
	 PortfolioDTO getPortfoliobyId( String portfolioId);
	 List<PortfolioDTO> getAllPortfolios();
	 void deletePortfolio(String portfolioId);
}
