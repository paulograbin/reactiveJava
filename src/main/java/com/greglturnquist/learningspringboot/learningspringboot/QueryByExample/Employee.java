package com.greglturnquist.learningspringboot.learningspringboot.Example;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document
public class Employee {

    @Id
    private String idl;
    private String lastName;
    private String firstName;
    private String role;

}
