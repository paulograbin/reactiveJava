package com.paulograbin.learningspringboot.chat;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;


@Component
public class SpringDataUserDetailsRepository implements ReactiveUserDetailsService {

    @Resource
    private UserRepository repository;


    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return repository.findByUsername(username)
                .map(user -> User.withDefaultPasswordEncoder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .authorities(user.getRoles())
                        .build());
    }
}
