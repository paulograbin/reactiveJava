package com.greglturnquist.learningspringboot.comments;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
public class CommentController {

    private final CommentRepository repository;

    public CommentController(CommentRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/comment/{imageId}")
    public Flux<Comment> getComments(@PathVariable String imageId) {
        System.out.println("Fetching comments for " + imageId);

        return repository.findByImageId(imageId);
    }

    @GetMapping("/comment")
    public Flux<Comment> getAllComments() {
        System.out.println("Fetching comments for all images");

        return repository.findAll();
    }
}
