package com.citb.app.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ApiException extends RuntimeException {
	
	public ApiException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
   
}
