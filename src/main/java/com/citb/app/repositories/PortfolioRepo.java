package com.citb.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.citb.app.entities.Portfolio;

public interface PortfolioRepo extends JpaRepository<Portfolio, Integer>{

}
