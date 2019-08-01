package com.greglturnquist.learningspringboot.comments;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@Document
public class Comment {

    @Id
    private String id;
    private String imageId;
    private String comment;
    private LocalDateTime creationDate = LocalDateTime.now();

}
