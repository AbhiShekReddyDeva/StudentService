package com.example.StudentManagement.exception;

public class StudentNotFoundException extends RuntimeException
{
    private String message;

    @Override
    public String getMessage() {
        return message;
    }
    public StudentNotFoundException(String message)
    {
        this.message=message;
    }
}
