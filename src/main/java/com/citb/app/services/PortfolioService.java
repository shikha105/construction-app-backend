package com.citb.app.services;

import java.util.List;

import com.citb.app.payloads.PortfolioDTO;

public interface PortfolioService {

	 PortfolioDTO createPortfolio(PortfolioDTO portfolioDTO);
	 PortfolioDTO updatePortfolio(PortfolioDTO portfolioDTO, Integer portfolioId);
	 PortfolioDTO getPortfoliobyId( Integer portfolioId);
	 List<PortfolioDTO> getAllPortfolios();
	 void deletePortfolio(Integer portfolioId);
}
