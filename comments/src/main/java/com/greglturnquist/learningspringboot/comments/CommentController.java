package com.greglturnquist.learningspringboot.comments;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @DeleteMapping("/comment/{imageId}")
    public Mono<Void> deleteCommentsByImageId(@PathVariable String imageId) {
        System.out.println("Deleting every comment of " + imageId);

        return commentService.deleteCommentsByImageId(imageId);
    }

    @DeleteMapping("/comment/delete/{commentId}")
    public Mono<Void> deleteCommentById(@PathVariable String commentId) {
        System.out.println("Controller - Deleting comment " + commentId);

        return commentService.deleteCommentByID(commentId);
    }

    @GetMapping("/comment/{imageId}")
    public Flux<Comment> getComments(@PathVariable String imageId) {
        System.out.println("Returning comments for " + imageId);

        return commentService.findByImageId(imageId);
    }

    @GetMapping("/comment")
    public Flux<Comment> getAllComments() {
        System.out.println("Returning comments for all images");

        return commentService.findAllComments();
    }
}
