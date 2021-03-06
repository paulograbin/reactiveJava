package com.greglturnquist.learningspringboot.images;

import lombok.Data;
import org.springframework.data.annotation.Id;


@Data
//@Document
public class Image {

    /**
     * @Data is a Lombok annotation that generates getters, toString, hashCode, equals as
     * well as setters for all non-final fields
     * @NoArgsConstructor is a Lombok annotation to generate a no-argument
     * constructor
     * It has id and name fields for storing data
     * We have crafted a custom constructor to load up fields of data
     */

    @Id
    private String id;
    private String name;
    private String comment;

    public Image(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
