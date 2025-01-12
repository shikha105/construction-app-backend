package com.citb.app.services;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface AmazonS3Service {
	
	public String uploadImage(MultipartFile image);
	public List<String> getAllImages();
	public String preSignedUrl(String imageName);
	public String getImageUrlByName(String imageName);
	public void deleteImage(String imageName);
}
