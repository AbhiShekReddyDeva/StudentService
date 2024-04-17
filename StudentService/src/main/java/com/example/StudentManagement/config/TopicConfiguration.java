package com.example.StudentManagement.config;


import com.example.StudentManagement.util.AppConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfiguration {

    @Bean
    public NewTopic addStudent()
    {
        return TopicBuilder.name("add-student").partitions(2).replicas(1).build();
    }

    @Bean
    public NewTopic deleteStudent(){
        return TopicBuilder.name(AppConstants.DELETE_STUDENT_TOPIC).build();
    }

    @Bean
    public NewTopic updateStudent()
    {
        return TopicBuilder.name(AppConstants.UPDATE_STUDENT_TOPIC).build();
    }
}

