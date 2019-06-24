package com.greglturnquist.learningspringboot.comments;

import com.greglturnquist.learningspringboot.images.Comment;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface CommentWriterRepository extends org.springframework.data.repository.Repository<Comment, String> {

    Mono<Comment> save(Comment newComment);

    Mono<Comment> findById(String id);

    Flux<Comment> saveAll(Flux<Comment> newComments);
}
