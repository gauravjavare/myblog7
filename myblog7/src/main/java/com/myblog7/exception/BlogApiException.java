package com.myblog7.exception;

import org.springframework.http.HttpStatus;

public class BlogApiException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public BlogApiException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
    public BlogApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
