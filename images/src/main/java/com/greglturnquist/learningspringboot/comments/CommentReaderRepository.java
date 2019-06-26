package com.greglturnquist.learningspringboot.comments;

import org.springframework.data.repository.Repository;
import reactor.core.publisher.Flux;


@org.springframework.stereotype.Repository
public interface CommentReaderRepository extends Repository<Comment, String> {

    Flux<Comment> findByImageId(String imageId);

}
