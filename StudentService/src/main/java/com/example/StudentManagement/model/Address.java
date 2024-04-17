package com.example.StudentManagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Address {
    private String city;
    private int pincode;
    public Address(){}
    public Address(String city,int pincode)
    {
        this.city = city;
        this.pincode = pincode;
    }
}
