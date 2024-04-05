package com.naz.libManager.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.naz.libManager.payload.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class LibManagerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(LibManagerException.class)
    public ResponseEntity<ApiResponse<String>> handleLibManagerException(LibManagerException exception){
        ApiResponse<String> response = new ApiResponse<>(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ApiResponse<String> handleUsernameNotFoundException(UsernameNotFoundException exception){
        return new ApiResponse<>(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(LockedException.class)
    public ApiResponse<String> handleLockedException(LockedException exception){
        return new ApiResponse<>(
                "Email is not verified. Check your email for verification link",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ApiResponse<String> handleJsonProcessingException(JsonProcessingException exception){
        return new ApiResponse<>(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler(HttpClientErrorException.class)
    public ApiResponse<String> handleHttpClientErrorException(HttpClientErrorException exception){
        return new ApiResponse<>(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponse<String> handleConstraintViolationException(ConstraintViolationException exception){
        return new ApiResponse<>(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ApiResponse<String> handleBadCredentialsException(){
        return new ApiResponse<>(
                "Incorrect user details",
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ApiResponse<String> handleExpiredJwtException(){
        return new ApiResponse<>(
                "Token is expired. Try login or request for another",
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ApiResponse<String> handleMalformedJwtException(){
        return new ApiResponse<>(
                "Incorrect token",
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(UnknownHostException.class)
    public ApiResponse<String> handleUnknownHostException(){
        return new ApiResponse<>(
                "An error occurred while handling request. Check your internet connection.",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ApiResponse<String> handleServerException(){
        return new ApiResponse<>(
                "Invalid input.",
                HttpStatus.BAD_REQUEST
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request
    ) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });
        ApiResponse<List<String>> response = new ApiResponse<>();
        response.setData(errors);
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setMessage("Validation error");
        response.setStatusCode(status.value());
        return new ResponseEntity<>(response, response.getStatus());
    }
}
