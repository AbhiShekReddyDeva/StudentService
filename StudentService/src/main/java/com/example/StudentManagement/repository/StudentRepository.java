package com.example.StudentManagement.repository;


import com.example.StudentManagement.model.Student;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends MongoRepository<Student,Integer>
{
    Student findStudentByRollNum(int rollNum);
    Student deleteStudentByRollNum(int rollNum);
    @Aggregation({"{ $unwind: '$address' } " ,
            "{ $group: { _id: '$address.city' }}"})
    List<String> getUniqueCities();

}
