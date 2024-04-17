package com.example.StudentManagement.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

@Data
public class ErrorResponse {
    private String message;
    private LocalDate timeStamp;
    private HttpStatus errorCode;
}
