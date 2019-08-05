package com.spring.ehcache.handler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.spring.ehcache.common.CommonConstantValue;
import com.spring.ehcache.config.AppErrorConfig;


@ControllerAdvice
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {
	@Autowired
	private AppErrorConfig appErrorConfig;
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
    	 Map<String, Object> body = new LinkedHashMap<>();
        //Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());
       
        body.put(CommonConstantValue.DATA, errors);
        
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(CommonConstantValue.MESSAGE);
        messageBuilder.append(this.appErrorConfig.getValidation());
        messageBuilder.append(CommonConstantValue.PIPE);
        messageBuilder.append(CommonConstantValue.ERROR_SOURCE);
        messageBuilder.append(ex.getParameter().getContainingClass().getName());
        messageBuilder.append(CommonConstantValue.PIPE);
        messageBuilder.append(CommonConstantValue.ERROR_METHOD);
        messageBuilder.append(ex.getParameter().getMethod().getName());
        
        body.put(CommonConstantValue.MESSAGE, messageBuilder.toString());
        body.put(CommonConstantValue.STATUS, false);
        return new ResponseEntity( body, HttpStatus.BAD_REQUEST);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
			TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {    
    	
    	 Map<String, Object> body = new LinkedHashMap<>();
    	 
    	 StringBuilder messageBuilder = new StringBuilder();
         messageBuilder.append(CommonConstantValue.MESSAGE);
         messageBuilder.append(this.appErrorConfig.getTypeMatching());
         messageBuilder.append(CommonConstantValue.PIPE);
         messageBuilder.append(CommonConstantValue.ERROR_SOURCE);
         messageBuilder.append(ex.getMessage().substring(0, ex.getMessage().indexOf(';')));
        
         body.put(CommonConstantValue.MESSAGE, messageBuilder.toString());
         body.put(CommonConstantValue.STATUS, false);
         return new ResponseEntity( body, HttpStatus.BAD_REQUEST);
	}
}