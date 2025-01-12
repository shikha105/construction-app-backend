package com.citb.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.citb.app.payloads.MeetingDTO;
import com.citb.app.services.AmazonS3Service;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/images")
public class S3Controller {
	
	 @Autowired
	 private AmazonS3Service amazonS3Service;
	 
	
	@PostMapping
	public ResponseEntity<?> uploadImage(@RequestParam MultipartFile image){
		return  ResponseEntity.ok(this.amazonS3Service.uploadImage(image));
	}
	
	@GetMapping
	public List<String> getAllImages(){
		return  amazonS3Service.getAllImages();
	}
	
	@GetMapping("/{imageName}")
	public String getImageUrlByName(@PathVariable("imageName") String imageName){
		return  amazonS3Service.getImageUrlByName(imageName);
	}
}
