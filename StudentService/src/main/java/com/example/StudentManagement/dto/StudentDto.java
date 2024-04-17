package com.example.StudentManagement.dto;




import com.example.StudentManagement.model.Address;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentDto
{
    private int rollNum;
    private String studentName;
    private int age;
    private List<Address> address;

    @Override
    public String toString()
    {
        return "Student:{ studentName : "+this.studentName+" age : "+ this.age +" address : "+this.address +"}";
    }
}
