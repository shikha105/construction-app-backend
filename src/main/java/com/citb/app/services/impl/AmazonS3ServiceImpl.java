package com.citb.app.services.impl;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.citb.app.config.AppConstants;
import com.citb.app.exceptions.ImageUploadException;
import com.citb.app.services.AmazonS3Service;




@Service
public class AmazonS3ServiceImpl implements AmazonS3Service{

	

	@Autowired
	AmazonS3 amazonS3;
	
	@Value("${app.s3.bucket}")
	private String bucketName;
	
	
	@Override
	public String uploadImage(MultipartFile image, String folderName) {
		
		if(image == null || image.isEmpty()) {
			throw new ImageUploadException("Image is null or empty ");
		}
		String originalFileName = image.getOriginalFilename();
		
		if(originalFileName == null || originalFileName.isEmpty()) {
			throw new ImageUploadException("image file name is null or empty ");
		}
		
		String fileExtension= originalFileName.substring(originalFileName.lastIndexOf('.') +1).toLowerCase();
		String imageName = UUID.randomUUID().toString() + "." + fileExtension;
		
		ObjectMetadata metaData= new ObjectMetadata();
		metaData.setContentLength(image.getSize());
		
		try {
			
			 boolean folderExists = amazonS3.listObjectsV2(bucketName, folderName + "/")
                     .getKeyCount() > 0;
                     
                     if (!folderExists) {
                         ObjectMetadata folderMetadata = new ObjectMetadata();
                         folderMetadata.setContentLength(0);
                         amazonS3.putObject(bucketName, folderName + "/", new ByteArrayInputStream(new byte[0]), folderMetadata);
                     }       
                     
			amazonS3.putObject(new PutObjectRequest(bucketName, folderName +"/"+imageName, image.getInputStream(), metaData));
		} catch (IOException e) {
				
			throw new ImageUploadException("error in uploading image" + e.getMessage());
		}
		
		
		return imageName;
	}

	@Override
	public List<String> getAllImages() {
		List<String> imageUrls = new ArrayList<>();
		ListObjectsV2Result result = amazonS3.listObjectsV2(bucketName);
		for(S3ObjectSummary summary : result.getObjectSummaries()) {
			imageUrls.add(this.preSignedUrl(summary.getKey()));
		}
		
		return imageUrls;
	}

	@Override
	public String preSignedUrl(String imageName) {
		final long expirationTimeMillis = System.currentTimeMillis() + AppConstants.HOURS * 60 * 60 * 1000L;
		final Date expirationDate = new Date(expirationTimeMillis);
		GeneratePresignedUrlRequest generatePreSignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, imageName)
				.withMethod(HttpMethod.GET)
				.withExpiration(expirationDate);
				
		URL url = amazonS3.generatePresignedUrl(generatePreSignedUrlRequest);
		return url.toString();
	}

	@Override
	public String getImageUrlByName(String imageName) {
        S3Object object = amazonS3.getObject(bucketName, imageName);
        String key = object.getKey();
        String url= this.preSignedUrl(key);
		return url;
	}

	@Override
	public void deleteImage(String imageName) {
		amazonS3.deleteObject(bucketName, imageName);
		
	}

	
	

}
