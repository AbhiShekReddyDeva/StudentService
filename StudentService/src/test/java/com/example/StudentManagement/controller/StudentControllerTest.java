package com.example.StudentManagement.controller;

import com.example.StudentManagement.dto.StudentDto;
import com.example.StudentManagement.model.Student;
import com.example.StudentManagement.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentService studentService;
    @InjectMocks
    private StudentController studentController;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testAddStudentSuccess() throws Exception {
        // given
//        StudentDto studentDto = StudentDto.builder()
//                .rollNum(2)
//                .studentName("Naveen Reddy")
//                .build();

        StudentDto studentDto = new StudentDto();

        ResponseEntity<String> expected = new ResponseEntity<>("Student added successfully",HttpStatus.CREATED);
        when(studentService.addStudent(Mockito.any())).thenReturn(expected);

        String jsonStudent = objectMapper.writeValueAsString(studentDto);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonStudent))
                .andExpect(status().isCreated())
                .andExpect(content().string(expected.getBody()));
    }


    @Test
    void testAddStudentWhenStudentAlreadyExist() throws Exception
    {
        StudentDto studentDto = StudentDto.builder()
                .rollNum(2)
                .studentName("Naveen Reddy").build();
        ResponseEntity<String> expected = new ResponseEntity<>("Student with rollNum "+studentDto.getRollNum()+" already exist", HttpStatus.CONFLICT);
        given(studentService.addStudent(studentDto)).willReturn(expected);

        String jsonStudent = objectMapper.writeValueAsString(studentDto);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStudent))
                .andExpect(status().isConflict());
    }

    @Test
    void testGetStudentSuccess() throws Exception {

        int rollNum = 1;
        StudentDto studentDto = StudentDto.builder()
                .rollNum(2)
                .studentName("Naveen Reddy").build();
        Student student = new Student(studentDto);
        ResponseEntity<Student> expected = new ResponseEntity<>(student, HttpStatus.OK);

        when(studentService.getStudent(rollNum)).thenReturn(expected);

        mockMvc.perform(get("/students")
                        .param("rollNum", String.valueOf(rollNum)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetStudentWhenStudentNotExist() throws Exception {
        int rollNum = 1;

        ResponseEntity<Student> expected = new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        when(studentService.getStudent(eq(rollNum))).thenReturn(expected);

        mockMvc.perform(get("/students")
                        .param("rollNum", String.valueOf(rollNum))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Expecting a Not Found (404) status
    }
    @Test
    void testDeleteStudentSuccess() throws Exception
    {
        //given
        int rollNum = 1;
        ResponseEntity<String> expected = new ResponseEntity<>("Deleted successfully",HttpStatus.OK);
        given(studentService.deleteStudent(rollNum)).willReturn(expected);

        mockMvc.perform(delete("/students")
                .param("rollNum",String.valueOf(rollNum))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void testDeleteStudentWhenStudentNotExist() throws Exception
    {
        int rollNum = 1;
        ResponseEntity<String> expected = new ResponseEntity<>("Student with rollNum "+rollNum+" not found",HttpStatus.NOT_FOUND);
        given(studentService.deleteStudent(rollNum)).willReturn(expected);

        mockMvc.perform(delete("/students")
                        .param("rollNum",String.valueOf(rollNum))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    void testUpdateStudentSuccess() throws Exception
    {
        StudentDto studentDto = StudentDto.builder()
                .rollNum(2)
                .studentName("Naveen Reddy").build();
        ResponseEntity<String> expected = new ResponseEntity<>("updated successfully",HttpStatus.OK);
        given(studentService.updateStudent(studentDto)).willReturn(expected);
        mockMvc.perform(put("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isOk());
    }
    @Test
    void testUpdateStudentWhenStudentNotFound() throws Exception
    {
        StudentDto studentDto = StudentDto.builder()
                .rollNum(2)
                .studentName("Naveen Reddy").build();
        ResponseEntity<String> expected = new ResponseEntity<>("Student with rollNum "+studentDto.getRollNum()+" not found",HttpStatus.NOT_FOUND);
        given(studentService.updateStudent(studentDto)).willReturn(expected);
        mockMvc.perform(put("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isNotFound());
    }

}
