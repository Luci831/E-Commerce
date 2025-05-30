package com.ecommerce.project.exception;

import java.util.*;

import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ecommerce.project.payload.APIResponse;

@RestControllerAdvice
public class MyGlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		Map<String, String> response = new HashMap<>();
		
		e.getBindingResult().getAllErrors().forEach(err->{
			String fieldName= ((FieldError) err).getField();
			String message= err.getDefaultMessage();
			
			response.put(fieldName,message);
		});
		

		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);

	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<APIResponse> myResourceNotFound(ResourceNotFoundException e)
	{
		String message=e.getMessage();
		APIResponse apiResponse=new APIResponse(message,false);
		
		return new ResponseEntity<APIResponse>(apiResponse,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<APIResponse> myApiException(ApiException e)
	{
		
		String message=e.getMessage();
		APIResponse apiResponse=new APIResponse(message,false);
		
		
		return new ResponseEntity<APIResponse>(apiResponse,HttpStatus.BAD_REQUEST);
	}

}
