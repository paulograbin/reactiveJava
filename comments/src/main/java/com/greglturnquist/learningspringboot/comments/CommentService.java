package com.greglturnquist.learningspringboot.comments;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@EnableBinding(Processor.class)
public class CommentService {

    private final static Logger log = LoggerFactory.getLogger(CommentService.class);

    private CommentRepository commentRepository;

    private final MeterRegistry meterRegistry;

    public CommentService(CommentRepository commentRepository, MeterRegistry meterRegistry) {
        this.commentRepository = commentRepository;
        this.meterRegistry = meterRegistry;
    }

    @StreamListener
    @Output(Processor.OUTPUT)
    public Flux<Comment> save(@Input(Processor.INPUT) Flux<Comment> newComments) {
        return commentRepository
                .saveAll(newComments)
                .map(comment -> {
                    log.info("Saving new comment " + comment);
                    meterRegistry.counter("comments.consumed", "imageId", comment.getImageId())
                            .increment();

                    return comment;
                });
    }

    public Flux<Comment> findAllComments() {
        return commentRepository.findAll();
    }

    public Mono<Void> deleteCommentByID(String commentId) {
        System.out.println("Service - Deleting comment " + commentId);

        return commentRepository.deleteCommentById(commentId).log("Service - deleting " + commentId).then();
    }
}
