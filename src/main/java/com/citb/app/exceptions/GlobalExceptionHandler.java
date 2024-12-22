package com.citb.app.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.citb.app.payloads.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){
	
		
		String message = ex.getMessage();
		
		return new ResponseEntity<ApiResponse>(new ApiResponse(message, false), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex){
		
		Map<String, String> mapResponse = new HashMap<>();
		
		ex.getBindingResult().getAllErrors().forEach((error) -> {
		
		String fieldName = ((FieldError)error).getField();
		String fieldMessage = error.getDefaultMessage();
		
		mapResponse.put(fieldName, fieldMessage);	
		});
		
		return new ResponseEntity<>(mapResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiResponse> apiExceptionHandler(ApiException ex){
	
		
		String message = ex.getMessage();
		
		return new ResponseEntity<ApiResponse>(new ApiResponse(message, false), HttpStatus.BAD_REQUEST);
	}

}
