package com.greglturnquist.learningspringboot.comments;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface CommentWriterRepository extends org.springframework.data.repository.Repository<Comment, String> {

    Mono<Comment> save(Comment newComment);

    Mono<Comment> findById(String id);

    Flux<Comment> findByImageId(String imageId);

    Flux<Comment> saveAll(Flux<Comment> newComments);
}
