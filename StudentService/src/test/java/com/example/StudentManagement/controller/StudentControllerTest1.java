package com.example.StudentManagement.controller;

import com.example.StudentManagement.dto.StudentDto;
import com.example.StudentManagement.exception.DuplicateStudentException;
import com.example.StudentManagement.exception.StudentNotFoundException;
import com.example.StudentManagement.model.Address;
import com.example.StudentManagement.model.Student;
import com.example.StudentManagement.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class StudentControllerTest1 {

    @Mock
    private StudentService studentService;
    @InjectMocks
    private StudentController studentController;

    public StudentDto getStudentDto()
    {
        Address address = new Address("Pileru",517214);
        Address address1 = new Address("Tirupathi",517507);
        List<Address> addressList = new ArrayList<>();
        addressList.add(address);
        addressList.add(address1);
        return new StudentDto(1,"Abhishek",23,addressList);
    }

    @Test
    void testAddStudent(){

        //given
        StudentDto studentDto = getStudentDto();
        //expected
        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.CREATED)
                .body("Student added successfully");

        when(studentService.addStudent(studentDto)).thenReturn(expected);
        //actual
        ResponseEntity<String> actual = studentController.addStudent(studentDto);
        //assertions
        assertEquals(expected.getStatusCode(),actual.getStatusCode());
        assertEquals(expected.getBody(),actual.getBody());
    }

    @Test
    void testAddStudentWhenStudentAlreadyExist()
    {
        //given
        StudentDto studentDto = getStudentDto();
        //expected
        ResponseEntity<String> expected = new ResponseEntity<>("Student with rollNum "+studentDto.getRollNum()+" already exist", HttpStatus.CONFLICT);

        when(studentService.addStudent(studentDto)).thenReturn(expected);
        //actual
        ResponseEntity<String> actual = studentController.addStudent(studentDto);
        //assertions
        assertEquals(expected.getStatusCode(),actual.getStatusCode());
        assertEquals(expected.getBody(),actual.getBody());
    }

    @Test
    public void testGetStudent() {
        // Given
        int rollNum = 1;
        StudentDto studentDto = getStudentDto();
        Student student = new Student(studentDto);
        //expected
        ResponseEntity<Student> expected = ResponseEntity.ok(student);

        when(studentService.getStudent(rollNum)).thenReturn(expected);

        ResponseEntity<Student> actual = studentController.getStudent(rollNum);

        assertEquals(expected.getStatusCode(), actual.getStatusCode());
        assertEquals(expected.getBody().getRollNum(), actual.getBody().getRollNum());
    }


    @Test
    void testGetStudentWhenStudentNotFound()
    {
        int rollNum = 1;
        StudentDto studentDto = getStudentDto();
        Student student = new Student(studentDto);
        //expected
        ResponseEntity<Student> expected = new ResponseEntity<>(null,HttpStatus.NOT_FOUND);

        when(studentService.getStudent(rollNum)).thenReturn(expected);

        ResponseEntity<Student> actual = studentController.getStudent(rollNum);

        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }

    @Test
    void testDeleteStudent()
    {
        int rollNum = 1;
        ResponseEntity<String> expected = new ResponseEntity<>("Deleted successfully",HttpStatus.OK);
        when(studentService.deleteStudent(rollNum)).thenReturn(expected);

        ResponseEntity<String> actual = studentController.deleteStudent(rollNum);
        assertEquals(expected.getStatusCode(),actual.getStatusCode());
        assertEquals(expected.getBody(),actual.getBody());
    }
    @Test
    void testDeleteStudentWhenStudentNotFound()
    {
        int rollNum = 1;
        ResponseEntity<String> expected = new ResponseEntity<>("Student with rollNum "+rollNum+" not found",HttpStatus.NOT_FOUND);
        when(studentService.deleteStudent(rollNum)).thenReturn(expected);

        ResponseEntity<String> actual = studentController.deleteStudent(rollNum);
        assertEquals(expected.getStatusCode(),actual.getStatusCode());
        assertEquals(expected.getBody(),actual.getBody());
    }

    @Test
    void testUpdateStudent()
    {
        StudentDto studentDto = getStudentDto();
        ResponseEntity<String> expected = new ResponseEntity<>("updated successfully",HttpStatus.OK);
        when(studentService.updateStudent(studentDto)).thenReturn(expected);

        ResponseEntity<String> actual = studentController.updateStudent(studentDto);
        assertEquals(expected.getStatusCode(),actual.getStatusCode());
        assertEquals(expected.getBody(),actual.getBody());
    }
    @Test
    void testUpdateStudent_WhenStudentNotFound() {
        StudentDto studentDto = getStudentDto();
        ResponseEntity<String> expected = new ResponseEntity<>("Student with rollNum "+studentDto.getRollNum()+" not found",HttpStatus.NOT_FOUND);
        when(studentService.updateStudent(studentDto)).thenReturn(expected);

        ResponseEntity<String> actual = studentController.updateStudent(studentDto);
        assertEquals(expected.getStatusCode(),actual.getStatusCode());
        assertEquals(expected.getBody(),actual.getBody());
    }

}