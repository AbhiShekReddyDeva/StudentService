package com.example.StudentManagement.model;


import com.proto.NotificationResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    private int notificationId;
    private String notificationType;
    private Student payload;
    private String createdOn;

    @Override
    public String toString()
    {
        return "Notification : { notificationType : "+this.notificationType+", payload : "+this.payload+", createdOn : "+this.createdOn+" }";
    }
}
