package com.paulograbin.learningspringboot.chat;

import reactor.core.publisher.Mono;
import org.springframework.data.repository.Repository;


public interface UserRepository extends Repository<User, String> {

    Mono<User> findByUsername(String username);
}
