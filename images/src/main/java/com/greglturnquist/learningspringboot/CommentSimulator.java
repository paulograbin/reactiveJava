package com.greglturnquist.learningspringboot;

import com.greglturnquist.learningspringboot.images.CommentController;
import com.greglturnquist.learningspringboot.images.ImageRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.validation.support.BindingAwareModelMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Profile("simulator")
@Component
public class CommentSimulator {

    private final HomeController homeController;
    private final CommentController controller;
    private final ImageRepository imageRepository;

    private final AtomicInteger counter;


    public CommentSimulator(HomeController homeController, CommentController controller, ImageRepository imageRepository) {
        this.homeController = homeController;
        this.controller = controller;
        this.imageRepository = imageRepository;
        this.counter = new AtomicInteger(1);
    }

//    @EventListener
//    public void simulateComments(ApplicationReadyEvent event) {
//        Flux
//                .interval(Duration.ofMillis(1000))
//                .flatMap(tick -> imageRepository.findAll())
//                .map(image -> {
//                    Comment comment = new Comment();
//                    comment.setImageId(image.getId());
//                    comment.setComment("Comment #" + counter.getAndIncrement());
//
//                    return Mono.just(comment);
//                })
//                .flatMap(newComment ->
//                        Mono.defer(() ->
//                                controller.addComment(newComment)))
//                .subscribe();
//    }


//    @Scheduled(fixedRate = 100)
//    public void simulateActivity() {
//        imageRepository.findAll()
//                .map(image -> {
//                    Comment c = new Comment();
//                    c.setImageId(image.getId());
//                    c.setComment("Comment #" + counter.get());
//
//                    return Mono.just(c);
//                })
//                .map(controller::addComment)
//                .subscribe();
//    }

    @EventListener
    public void simulateUsersClicking(ApplicationReadyEvent event) {
        Flux.interval(Duration.ofMillis(500))
                .flatMap(tick ->
                        Mono.defer(() ->
                                homeController.index(new BindingAwareModelMap())))
                .subscribe();
    }

//    @EventListener
//    public void onApplicationReadyEvent(ApplicationReadyEvent event) {
//        simulateComments(event);
//    }
}
