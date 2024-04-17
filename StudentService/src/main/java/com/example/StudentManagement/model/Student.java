package com.example.StudentManagement.model;


import com.example.StudentManagement.dto.StudentDto;
import com.proto.AddressResponse;
import com.proto.AddressResponseList;
import com.proto.StudentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Document(collection = "Student")
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    private int rollNum;
    private String studentName;
    private int age;
    private List<Address> address;

    public Student(StudentResponse studentResponse)
    {
        this.studentName = studentResponse.getStudentName();
        this.rollNum = studentResponse.getRollNum();
        this.age = studentResponse.getAge();
        AddressResponseList addressResponseList = studentResponse.getAddresses();
        List<AddressResponse> addressResponses = addressResponseList.getAddressList();
        address = new ArrayList<>();
        for(AddressResponse addressResponse : addressResponses)
        {
           Address address1 = new Address(addressResponse.getCity(),addressResponse.getPincode());
           address.add(address1);
        }
        System.out.println(address);

    }

    public Student(StudentDto studentDto)
    {
        this.studentName = studentDto.getStudentName();
        this.age = studentDto.getAge();
        this.rollNum = studentDto.getRollNum();
        this.address = studentDto.getAddress();
    }
    @Override
    public String toString() {
        return "Student : { rollNum : "+this.rollNum+", studentName : "+this.studentName+", age : "+this.age+", address : "+this.address+"}";
    }
}
