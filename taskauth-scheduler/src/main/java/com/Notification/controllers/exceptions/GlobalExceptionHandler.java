package com.Notification.controllers.exceptions;

import com.Notification.dtos.ErroResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler{

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> validationErrors = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrors.values());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity userNullException(NullPointerException nullPointerException, WebRequest webRequest){

        ErroResponseDto erroResponseDto = new ErroResponseDto(
                webRequest.getDescription(false),
                HttpStatus.UNPROCESSABLE_ENTITY,
                nullPointerException.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(erroResponseDto, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity usernameInvalidException(UsernameNotFoundException message,
                                                   WebRequest webRequest){

        ErroResponseDto erroResponseDto = new ErroResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                message.getMessage(),
                LocalDateTime.now());

        return new ResponseEntity(erroResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity passwordInvalidException(BadCredentialsException message,
                                                   WebRequest webRequest){

        ErroResponseDto erroResponseDto = new ErroResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                message.getMessage(),
                LocalDateTime.now());

        return new ResponseEntity(erroResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity handlerNotFound(NoSuchElementException noSuchException,
                                          WebRequest webRequest) {

        ErroResponseDto erroResponseDto = new ErroResponseDto(
                webRequest.getDescription(false),
                HttpStatus.NOT_FOUND,
                noSuchException.getMessage(),
                LocalDateTime.now());

        return new ResponseEntity(erroResponseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handleHttpMessageNotReadableException(HttpMessageNotReadableException exception,
                                            WebRequest webRequest){

        ErroResponseDto erroResponseDto = new ErroResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                "fill all fields correctly.",
                LocalDateTime.now());
        return new ResponseEntity(erroResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity unexpectedException(Exception exception,
                                              WebRequest webRequest) {

        String message = "something unexpected happened, see the logs";

        ErroResponseDto erroResponseDto = new ErroResponseDto(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR,
                message,
                LocalDateTime.now());

        logger.error(message, exception);
        return new ResponseEntity(erroResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
