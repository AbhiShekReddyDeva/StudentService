package com.example.StudentManagement.producer;

import com.example.StudentManagement.model.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class StudentProducer
{
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper;


    public void addStudent(String topic, int partition, String key, Student student)
    {
        try
        {
            String jsonStudent = objectMapper.writeValueAsString(student);
            kafkaTemplate.send(topic,partition,key,jsonStudent);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
    }
    public void addStudent(String topic, Student student)
    {
        try
        {
            String jsonStudent = objectMapper.writeValueAsString(student);
            kafkaTemplate.send(topic,jsonStudent);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }

    }
    public void deleteStudent(String topic,Student student)
    {
        try
        {
            String jsonStudent = objectMapper.writeValueAsString(student);
            kafkaTemplate.send(topic,jsonStudent);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();;
        }
    }
    public void updateStudent(String topic,Student student)
    {
        try
        {
            String jsonStudent = objectMapper.writeValueAsString(student);
            kafkaTemplate.send(topic,jsonStudent);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();;
        }
    }

}

