package com.sri.jwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.NOT_FOUND)
public class CompanyNameNotFoundException extends RuntimeException {
	
	public CompanyNameNotFoundException(String message) {
		
		
		super (message); 
	}
	
	
}
