package com.greglturnquist.learningspringboot.learningspringboot;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Image {

    /**
     * @Data is a Lombok annotation that generates getters, toString, hashCode, equals as
     * well as setters for all non-final fields
     * @NoArgsConstructor is a Lombok annotation to generate a no-argument
     * constructor
     * It has id and name fields for storing data
     * We have crafted a custom constructor to load up fields of data
     */

    private int id;
    private String name;

    public Image(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
