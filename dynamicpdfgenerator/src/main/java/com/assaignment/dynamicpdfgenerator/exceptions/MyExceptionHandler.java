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
		return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
		
	}
	@ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleRuntimeExceptions(RuntimeException ex) {
        System.err.println("ERRORRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR");
        return ResponseEntity.internalServerError().body("An error occurred while processing your request.");
    }
}
