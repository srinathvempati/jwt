package com.sri.jwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.FOUND)
public class UserNameExistException extends RuntimeException {
	
	public UserNameExistException(String message) {
		
		
		super (message); 
	}
}
