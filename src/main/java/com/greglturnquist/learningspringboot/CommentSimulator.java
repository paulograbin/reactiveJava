package com.greglturnquist.learningspringboot;

import com.greglturnquist.learningspringboot.comments.Comment;
import com.greglturnquist.learningspringboot.comments.CommentController;
import com.greglturnquist.learningspringboot.images.ImageRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CommentSimulator {

    private final CommentController controller;
    private final ImageRepository imageRepository;

    private final AtomicInteger counter;


    public CommentSimulator(CommentController controller, ImageRepository imageRepository) {
        this.controller = controller;
        this.imageRepository = imageRepository;
        this.counter = new AtomicInteger(1);
    }

    @EventListener
    public void onApplicationReadyEvent(ApplicationReadyEvent event) {
        Flux
                .interval(Duration.ofMillis(10))
                .flatMap(tick -> imageRepository.findAll())
                .map(image -> {
                    Comment comment = new Comment();
                    comment.setImageId(image.getId());
                    comment.setComment("Comment #" + counter.getAndIncrement());

                    return Mono.just(comment);
                })
                .flatMap(newComment ->
                        Mono.defer(() ->
                                controller.addComment(newComment)))
                .subscribe();
    }
}
