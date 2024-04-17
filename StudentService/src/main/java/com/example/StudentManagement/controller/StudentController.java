package com.example.StudentManagement.controller;

import com.example.StudentManagement.dto.StudentDto;
import com.example.StudentManagement.exception.DuplicateStudentException;
import com.example.StudentManagement.exception.StudentNotFoundException;
import com.example.StudentManagement.model.Notification;
import com.example.StudentManagement.model.Student;
import com.example.StudentManagement.service.StudentService;
import com.google.api.Http;
import com.google.protobuf.Descriptors;
import com.proto.NotificationRequest;
import com.proto.NotificationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<String> addStudent(@RequestBody StudentDto studentDto)
    {
        return studentService.addStudent(studentDto);
    }
    @PostMapping("/add")
    public boolean addNewStudent(@RequestBody StudentDto studentDto)
    {
        return studentService.addNewStudent(studentDto);
    }
    @GetMapping
    public ResponseEntity<Student> getStudent(@RequestParam int rollNum)
    {
       return studentService.getStudent(rollNum);
    }
    @DeleteMapping
    public  ResponseEntity<String> deleteStudent(@RequestParam int rollNum)
    {
        return studentService.deleteStudent(rollNum);
    }
    @PutMapping
    public ResponseEntity<String> updateStudent(@RequestBody StudentDto studentDto)
    {
        return studentService.updateStudent(studentDto);
    }

    @GetMapping("/unique-cities")
    public ResponseEntity<List<String>> getUniqueCities()
    {
        return new ResponseEntity<>(studentService.getUniqueCities(),HttpStatus.OK);
    }
    @GetMapping("/notification-by-id")
    public Map<Descriptors.FieldDescriptor,Object> getNotificationById(@RequestParam int notificationId)
    {
        NotificationRequest request = NotificationRequest.newBuilder().setNotificationId(notificationId).build();
        return studentService.getNotificationById(request);
    }

    @GetMapping("/paginated")
    public List<Notification> getNotificationsWithPagination(@RequestParam int pageNum,@RequestParam int pageSize)
    {
        return studentService.getNotificationsWithPagination(pageNum,pageSize);
    }

    @GetMapping("/sorted")
    public List<Notification> getNotificationWithSorting(@RequestParam int pageNum,@RequestParam int pageSize,@RequestParam String sortField,@RequestParam String direction)
    {
        return studentService.getNotificationWithSorting(pageNum,pageSize,sortField,direction);
    }


}
