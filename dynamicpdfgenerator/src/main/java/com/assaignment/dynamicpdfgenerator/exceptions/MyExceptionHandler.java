package com.assaignment.dynamicpdfgenerator.exceptions;


import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Map<String,String>> handleMethodArgumetExceptions(MethodArgumentNotValidException ex){
		Map<String ,String> map=new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach( error -> map.put(error.getField(),error.getDefaultMessage())
				);
		return new ResponseEntity<Map<String, String>>(map,HttpStatus.BAD_REQUEST);
		
	}

}
