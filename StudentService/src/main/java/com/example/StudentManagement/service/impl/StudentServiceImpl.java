package com.example.StudentManagement.service.impl;

import com.example.StudentManagement.dto.StudentDto;
import com.example.StudentManagement.exception.DuplicateStudentException;
import com.example.StudentManagement.exception.StudentNotFoundException;
import com.example.StudentManagement.model.Notification;
import com.example.StudentManagement.model.Student;
import com.example.StudentManagement.producer.StudentProducer;
import com.example.StudentManagement.repository.StudentRepository;
import com.example.StudentManagement.service.StudentService;
import com.example.StudentManagement.util.AppConstants;
import com.google.protobuf.Descriptors;
import com.proto.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService {

    ManagedChannel channel = ManagedChannelBuilder
            .forAddress("localhost", 9090)
            .usePlaintext() // Use plaintext for simplicity, you may want to use TLS in production
            .defaultLoadBalancingPolicy("round_robin") // Specify round-robin load balancing policy
            .build();

    StudentNotificationServiceGrpc.StudentNotificationServiceBlockingStub stub = StudentNotificationServiceGrpc.newBlockingStub(channel);
    StudentNotificationServiceGrpc.StudentNotificationServiceStub asyncStub = StudentNotificationServiceGrpc.newStub(channel);

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentProducer studentProducer;
    Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Override
    public ResponseEntity<String> addStudent(StudentDto studentDto) {
        Student student = studentRepository.findStudentByRollNum(studentDto.getRollNum());
        if (student == null) {
            Student student1 = new Student(studentDto);
            studentRepository.save(student1);
            char c = student1.getStudentName().trim().charAt(0);
            if ((c >= 'A' && c <= 'P') || (c >= 'a' && c <= 'p')) {
                studentProducer.addStudent(AppConstants.ADD_STUDENT_TOPIC, 0, "a-p", student1);
            } else if ((c > 'P' && c <= 'Z') || (c > 'p' && c <= 'z')) {
                studentProducer.addStudent(AppConstants.ADD_STUDENT_TOPIC, 1, "q-z", student1);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Student added successfully");
        }
      else {
            throw new DuplicateStudentException("Student with rollNum " + studentDto.getRollNum() + " already exists");
      }
    }

    @Override
    public boolean addNewStudent(StudentDto studentDto)
    {
        Student student = studentRepository.findStudentByRollNum(studentDto.getRollNum());
        if(student==null)
        {
            Student student1 = new Student(studentDto);
            studentRepository.save(student1);
            return true;
        }
        else {
            return false;
        }
    }
    @Override
    public ResponseEntity<Student> getStudent(int rollNum) {
        Student student = studentRepository.findStudentByRollNum(rollNum);
        if (student != null) {
            return new ResponseEntity<>(student, HttpStatus.OK);
        } else {
            throw new StudentNotFoundException("Student with rollNum " + rollNum + " not found");
        }
    }

    @Override
    public ResponseEntity<String> deleteStudent(int rollNum)
    {
        Student student = studentRepository.findStudentByRollNum(rollNum);
        if(student!=null)
        {
            studentRepository.deleteStudentByRollNum(rollNum);
            studentProducer.deleteStudent(AppConstants.DELETE_STUDENT_TOPIC,student);
            return new ResponseEntity<>("Deleted successfully",HttpStatus.OK);
        }
        else {
            throw new StudentNotFoundException("Student with rollNum "+rollNum+" not found");
        }

    }
    @Override
    public ResponseEntity<String> updateStudent(StudentDto studentDto)
    {
        Student student = studentRepository.findStudentByRollNum(studentDto.getRollNum());
        if (student!=null)
        {
            studentRepository.save(new Student(studentDto));
            studentProducer.updateStudent(AppConstants.UPDATE_STUDENT_TOPIC,student);
            return  new ResponseEntity<>("updated successfully",HttpStatus.OK);
        }
        else {
            throw new StudentNotFoundException("Student with rollNum "+studentDto.getRollNum()+" not found");
        }
    }
    public List<String> getUniqueCities()
    {
        return studentRepository.getUniqueCities();
    }
   public Map<Descriptors.FieldDescriptor,Object> getNotificationById(NotificationRequest request)
   {
       log.info(request.toString());
       NotificationResponse response =  stub.getNotificationById(request);
       return response.getAllFields();
   }
    public List<Notification> getNotificationsWithPagination(int pageNum, int pageSize)
    {
        PageRequest pageRequest = PageRequest.newBuilder().setPageNum(pageNum).setPageSize(pageSize).build();

        NotificationsResponseList responseList = stub.getAllNotification(pageRequest);
        log.info(responseList.toString());
        List<NotificationsResponse> notificationsResponseList = responseList.getNotificationsList();
        List<Notification> notifications = new ArrayList<>();
        for(NotificationsResponse notificationsResponse: notificationsResponseList)
        {
            notifications.add(mapToNotification(notificationsResponse));
        }
        return notifications;
    }
    public List<Notification> getNotificationWithSorting(int pageNum, int pageSize,String sortField,String direction)
    {
        SortRequest sortRequest = SortRequest.newBuilder().setPageNum(pageNum)
                .setPageSize(pageSize)
                .setSortField(sortField)
                .setDirection(direction).build();
        NotificationsResponseList notificationsResponseList = stub.getAllNotificationsWithSorting(sortRequest);
        List<NotificationsResponse> notificationsResponses = notificationsResponseList.getNotificationsList();
        List<Notification> notifications = new ArrayList<>();
        for(NotificationsResponse notificationsResponse : notificationsResponses)
        {
            notifications.add(mapToNotification(notificationsResponse));
        }
        return notifications;

    }
    private Notification mapToNotification(NotificationsResponse notificationsResponse)
    {
        Notification notification = new Notification();
        notification.setNotificationId(notificationsResponse.getNotificationId());
        notification.setNotificationType(notificationsResponse.getNotificationType());
        notification.setCreatedOn(notificationsResponse.getCreatedOn());
        StudentResponse studentResponse = notificationsResponse.getPayload();
        Student student = new Student(studentResponse);
        notification.setPayload(student);
        return notification;
    }
}
