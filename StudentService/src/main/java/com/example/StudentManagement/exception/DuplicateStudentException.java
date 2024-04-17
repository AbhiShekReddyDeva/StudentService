package com.example.StudentManagement.exception;

public class DuplicateStudentException extends RuntimeException
{

    private String message;

    public String getMessage() {
        return message;
    }
    public DuplicateStudentException(String message)
    {
        this.message = message;
    }
}