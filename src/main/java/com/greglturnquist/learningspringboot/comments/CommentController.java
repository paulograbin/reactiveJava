package com.greglturnquist.learningspringboot.comments;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;


@Profile("simulator")
@Controller
public class CommentController {

    private final RabbitTemplate rabbitTemplate;
    private final MeterRegistry meterRegistry;

    public CommentController(RabbitTemplate rabbitTemplate, MeterRegistry meterRegistry) {
        this.rabbitTemplate = rabbitTemplate;
        this.meterRegistry = meterRegistry;
    }

    /**
     * So our code wraps that inside a Java Runnable and converts it into a Mono via Reactor's
     * Mono.fromRunnable. That makes it possible to invoke this blocking task only when
     * we're ready at the right time.
     *
     * It's important to know that a Mono-wrapped-Runnable
     * doesn't act like a traditional Java Runnable and doesn't get launched in a separate
     * thread. Instead, the Runnable interface provides a convenient wrapper where Reactor
     * controls precisely when the run() method is invoked inside its scheduler.
     *
     * Spring AMQP doesn't yet support Reactive Streams. That is why
     * rabbitTemplate.convertAndSend() must be wrapped in Mono.fromRunnable.
     * Blocking calls such as this subscribe() method should be red flags, but
     * in this situation, it's a necessary evil until Spring AMQP is able to add
     * support. There is no other way to signal for this Reactor flow to execute
     * without it.
     */
    @PostMapping("/comments")
    public Mono<String> addComment(Mono<Comment> newComment) {
        return newComment.flatMap(comment ->
                Mono.fromRunnable(() ->
                        rabbitTemplate.convertAndSend("learning-spring-boot", "comments.new", comment))
                .then(Mono.just(comment)))
                .log("commentService-publish")
                .flatMap(comment -> {
                    meterRegistry.counter("comments.produced", "imagedId", comment.getImageId())
                            .increment();

                    return Mono.just("redirect:/");
                });
    }
}
