package com.example.StudentManagement.service.impl;

import com.example.StudentManagement.dto.StudentDto;
import com.example.StudentManagement.exception.DuplicateStudentException;
import com.example.StudentManagement.exception.StudentNotFoundException;
import com.example.StudentManagement.model.Student;
import com.example.StudentManagement.producer.StudentProducer;
import com.example.StudentManagement.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private StudentProducer studentProducer;
    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void testAddStudent()
    {
        StudentDto studentDto = StudentDto.builder().rollNum(2).studentName("ravi").build();
        Student student = new Student(studentDto);
        when(studentRepository.findStudentByRollNum(student.getRollNum())).thenReturn(null);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        ResponseEntity<String> actual = studentService.addStudent(studentDto);
        assertEquals(HttpStatus.CREATED,actual.getStatusCode());
        assertEquals("Student added successfully",actual.getBody());
    }
    @Test
    void testAddStudentWhenStudentAlreadyExist()
    {
        StudentDto studentDto = StudentDto.builder().rollNum(2).studentName("ravi").build();
        Student student = new Student(studentDto);
        when(studentRepository.findStudentByRollNum(studentDto.getRollNum())).thenReturn(student);
        assertThrows(DuplicateStudentException.class,()->{studentService.addStudent(studentDto);});

    }

    @Test
    void testGetStudentSuccess()
    {
        int rollNum = 1;
        StudentDto studentDto = StudentDto.builder().rollNum(1).studentName("Ravi").build();
        Student student = new Student(studentDto);
        when(studentRepository.findStudentByRollNum(rollNum)).thenReturn(student);

        ResponseEntity<Student> actual = studentService.getStudent(rollNum);

        assertEquals(HttpStatus.OK,actual.getStatusCode());
        assertEquals(student,actual.getBody());
    }

    @Test
    void testGetStudentWhenStudentNotExist()
    {
        int rollNum = 1;
        when(studentRepository.findStudentByRollNum(rollNum)).thenReturn(null);

        assertThrows(StudentNotFoundException.class,()->{studentService.getStudent(rollNum);});
    }

    @Test
    void testDeleteStudentSuccess()
    {
        int rollNum = 1;
        Student existingStudent = new Student();
        existingStudent.setRollNum(1);
        existingStudent.setStudentName("Raju");

        when(studentRepository.findStudentByRollNum(rollNum)).thenReturn(existingStudent);
        when(studentRepository.deleteStudentByRollNum(rollNum)).thenReturn(existingStudent);

        ResponseEntity<String> actual = studentService.deleteStudent(rollNum);
        assertEquals(HttpStatus.OK,actual.getStatusCode());
        assertEquals("Deleted successfully",actual.getBody());
    }

    @Test
    void testDeleteStudentWhenStudentNotFound()
    {
        int rollNum = 1;

        when(studentRepository.findStudentByRollNum(rollNum)).thenReturn(null);
        assertThrows(StudentNotFoundException.class,()->{studentService.deleteStudent(rollNum);});
    }

    @Test
    void testUpdateStudentSuccess()
    {
        StudentDto studentDto = StudentDto.builder().rollNum(2).studentName("Ramu").build();
        Student existingStudent = new Student();
        existingStudent.setRollNum(2);
        existingStudent.setStudentName("Rafi");
        when(studentRepository.findStudentByRollNum(studentDto.getRollNum())).thenReturn(existingStudent);
        when(studentRepository.save(any(Student.class))).thenReturn(new Student(studentDto));

        ResponseEntity<String> actual = studentService.updateStudent(studentDto);
        assertEquals(HttpStatus.OK,actual.getStatusCode());
        assertEquals("updated successfully",actual.getBody());
    }

    @Test
    void testUpdateStudentWhenStudentNotFound()
    {
        StudentDto studentDto = StudentDto.builder().rollNum(3).studentName("Sachin").build();
        when(studentRepository.findStudentByRollNum(studentDto.getRollNum())).thenReturn(null);
        assertThrows(StudentNotFoundException.class,()->{studentService.updateStudent(studentDto);});
    }

}