package com.paulograbin.learningspringboot.chat;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

abstract class UserParsingHandshakeHandler implements WebSocketHandler {

    private final Map<String, String> userMap;

    public UserParsingHandshakeHandler() {
        this.userMap = new HashMap<>();
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        this.userMap.put(session.getId(), Stream.of(session.getHandshakeInfo().getUri()
                .getQuery().split("&"))
                .map(s -> s.split("="))
                .filter(strings -> strings[0].equals("user"))
                .findFirst()
                .map(strings -> strings[1])
                .orElse(""));

        return handleInternal(session);
    }

    protected abstract Mono<Void> handleInternal(WebSocketSession session);

    String getUser(String id) {
        return userMap.get(id);
    }
}
