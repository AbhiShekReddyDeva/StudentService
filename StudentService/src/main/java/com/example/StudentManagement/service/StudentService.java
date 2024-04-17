package com.example.StudentManagement.service;

import com.example.StudentManagement.dto.StudentDto;
import com.example.StudentManagement.model.Notification;
import com.example.StudentManagement.model.Student;
import com.google.protobuf.Descriptors;
import com.proto.NotificationRequest;
import com.proto.NotificationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface StudentService
{
    ResponseEntity<String> addStudent(StudentDto studentDto);
    boolean addNewStudent(StudentDto studentDto);
    ResponseEntity<Student> getStudent(int rollNum);

    ResponseEntity<String> deleteStudent(int rollNum);
    ResponseEntity<String> updateStudent(StudentDto studentDto);

    List<String> getUniqueCities();
    Map<Descriptors.FieldDescriptor,Object> getNotificationById(NotificationRequest request);

    List<Notification>getNotificationsWithPagination(int pageNum, int pageSize);
    List<Notification> getNotificationWithSorting(int pageNum, int pageSize,String sortField,String direction);
}