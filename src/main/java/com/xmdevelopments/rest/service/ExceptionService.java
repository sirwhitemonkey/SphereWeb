package com.xmdevelopments.rest.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.xmdevelopments.model.Response;
import com.xmdevelopments.model.ResponseError;

@ControllerAdvice
public class ExceptionService extends ResponseEntityExceptionHandler {
	
    @ExceptionHandler({ Exception.class })
    protected ResponseEntity<Object> handleInvalidRequest(Exception e, WebRequest request) {
    	
    	
    	ResponseError responseError = new ResponseError();
    	Response response = new Response(responseError, HttpStatus.BAD_REQUEST.value());
    	
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
       return handleExceptionInternal(e, response, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

   
}