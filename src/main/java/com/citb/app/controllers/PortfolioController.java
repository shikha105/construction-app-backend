package com.citb.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import com.citb.app.payloads.ApiResponse;
import com.citb.app.payloads.MeetingDTO;
import com.citb.app.payloads.PortfolioDTO;
import com.citb.app.services.PortfolioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/portfolios")
public class PortfolioController {

	
	@Autowired
	private PortfolioService portService;
	
	//@PreAuthorize("hasRole('ROLE_APPRENTICE')")
	@PostMapping
	public ResponseEntity<PortfolioDTO> createPortfolio(
	        @Valid @RequestPart("portfolio") PortfolioDTO portfolioDTO, 
	        @RequestPart("images") List<MultipartFile> images){
		
		PortfolioDTO createdPortfolioDTO =	this.portService.createPortfolio(portfolioDTO, images);
		return new ResponseEntity<>(createdPortfolioDTO, HttpStatus.CREATED);
	}
	
	//@PreAuthorize("hasRole('ROLE_APPRENTICE')")
	@PutMapping("/{portfolioId}")
	public ResponseEntity<PortfolioDTO> updatePortfolio(
			@Valid @RequestPart("portfolio") PortfolioDTO portfolioDTO,
            @PathVariable String portfolioId,
            @RequestPart(value="images", required=false) List<MultipartFile> images){
		PortfolioDTO updatedPortfolioDTO =	this.portService.updatePortfolio(portfolioId, portfolioDTO, images);
		
		return new ResponseEntity<>(updatedPortfolioDTO, HttpStatus.OK);
	}
	
	@GetMapping("/{portfolioId}")
	public ResponseEntity<PortfolioDTO> getPortfolioById(@PathVariable  String portfolioId){
		PortfolioDTO portfolioDTO =	this.portService.getPortfolioById(portfolioId);
		
		
		return new ResponseEntity<>(portfolioDTO, HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<PortfolioDTO>> getAllPortfolios(){
		List<PortfolioDTO> portfolioDTOs =	this.portService.getAllPortfolios();
		
		
		return new ResponseEntity<>(portfolioDTOs, HttpStatus.OK);
	}
	
	@GetMapping("/getAll/{userId}")
	public ResponseEntity<List<PortfolioDTO>> getAllPortfoliosByUserId(@PathVariable  String userId){
		List<PortfolioDTO> portfolioDTOs =	this.portService.getAllPortfoliosByUserId(userId);
		
		
		return new ResponseEntity<>(portfolioDTOs, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_APPRENTICE')")
	@DeleteMapping("/{portfolioId}")
	public ResponseEntity<ApiResponse> deletePortfolio(@PathVariable String portfolioId){
			this.portService.deletePortfolio(portfolioId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Portfolio deleted successfully", true), HttpStatus.OK);
		
	}
	
	
}


