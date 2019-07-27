package com.greglturnquist.learningspringboot.comments;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
public class CommentController {

    private final CommentRepository repository;
    private final CommentService commentService;

    public CommentController(CommentRepository repository, CommentService commentService) {
        this.repository = repository;
        this.commentService = commentService;
    }


    @DeleteMapping("/comment/{imageId}")
    public Mono<Boolean> deleteCommentsByImageId(@PathVariable String imageId) {
        System.out.println("Deleting every comment of " + imageId);

        return repository.deleteAllByImageId(imageId);
    }

    @DeleteMapping("/comment/delete/{commentId}")
    public Mono<Void> deleteCommentById(@PathVariable String commentId) {
        System.out.println("Controller - Deleting comment " + commentId);

        return commentService.deleteCommentByID(commentId);
    }

    @GetMapping("/comment/count")
    public Mono<Integer> countComments() {
        System.out.println("Returning comment count");

        return repository.count();
    }

    @GetMapping("/comment/{imageId}")
    public Flux<Comment> getComments(@PathVariable String imageId) {
        System.out.println("Returning comments for " + imageId);

        return repository.findByImageId(imageId);
    }

    @GetMapping("/comment")
    public Flux<Comment> getAllComments() {
        System.out.println("Returning comments for all images");

        return repository.findAll();
    }
}
