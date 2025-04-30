package com.demo_loc_engine.demo.Services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class LOCValidation extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        Map response = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        response.put("rc", 400);
        response.put("rd", "error");
        response.put("detail", ex.getFieldErrors().get(0).getField()+ " " +ex.getFieldErrors().get(0).getDefaultMessage());
        return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
    }
}
