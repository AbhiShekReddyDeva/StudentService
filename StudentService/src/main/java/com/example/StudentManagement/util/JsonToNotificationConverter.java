package com.example.StudentManagement.util;

import com.example.StudentManagement.model.Notification;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class JsonToNotificationConverter {

    @Autowired
    private ObjectMapper objectMapper;
    public Notification convert(String jsonNotification) throws IOException
    {
        return objectMapper.readValue(jsonNotification, new TypeReference<Notification>() {});
    }

}
