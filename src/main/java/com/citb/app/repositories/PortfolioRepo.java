package com.citb.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.citb.app.entities.Portfolio;

public interface PortfolioRepo extends JpaRepository<Portfolio, String>{
	List<Portfolio> findByUserId(String userId);
}
