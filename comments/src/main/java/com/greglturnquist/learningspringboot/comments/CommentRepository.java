package com.greglturnquist.learningspringboot.comments;

import org.springframework.data.repository.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface CommentRepository extends Repository<Comment, String> {

    Mono<Integer> count();

    Mono<Boolean> deleteAllByImageId(String imageId);

    Mono<Boolean> deleteCommentById(String commentId);

    Flux<Comment> findAll();

    Mono<Comment> save(Comment newComment);

    Mono<Comment> findById(String id);

    Flux<Comment> findByImageId(String imageId);

    Flux<Comment> saveAll(Flux<Comment> newComments);
}
