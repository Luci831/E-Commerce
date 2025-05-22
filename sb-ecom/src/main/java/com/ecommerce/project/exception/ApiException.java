package com.ecommerce.project.exception;

public class ApiException extends RuntimeException{
	
	private static final long serialversionUID=1L;

	public ApiException() {
		
	}

	public  ApiException(String message) {
		super(message);
		
	}
	
	
	

}
