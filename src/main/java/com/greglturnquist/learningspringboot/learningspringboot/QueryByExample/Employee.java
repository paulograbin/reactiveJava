package com.greglturnquist.learningspringboot.learningspringboot.QueryByExample;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document
public class Employee {


    /**
     *
     * Lombok's @Data annotation provides getters, setters, equals, hashCode, and toString
     * methods
     * Spring Data MongoDB's @Document annotation indicates this POJO is a target for
     * storage in MongoDB
     * Spring Data Commons' @Id annotation indicates that the id field is the identifier
     * The rest of the fields are simple strings
     *
     */

    @Id
    private String id;
    private String lastName;
    private String firstName;
    private String role;

}
