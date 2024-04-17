package com.example.StudentManagement.consumer;


import com.example.StudentManagement.model.Notification;
import com.example.StudentManagement.util.JsonToNotificationConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class NotificationConsumer {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    @Autowired
    private JsonToNotificationConverter jsonToNotificationConverter;
    Logger log = LoggerFactory.getLogger(NotificationConsumer.class);
    @KafkaListener(topics = "notification",groupId = "0544")
    public void getNotification(String jsonNotification)
    {
        try
        {
            Notification notification = jsonToNotificationConverter.convert(jsonNotification);
            log.info(notification+"");

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
    @KafkaListener(topics = "deletedStudentNotification",groupId = "0544")
    public void consumeDeletedStudentNotification(String jsonNotification)
    {
        try
        {
            Notification notification = jsonToNotificationConverter.convert(jsonNotification);
            log.info(notification+"");

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}