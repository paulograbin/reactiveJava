package com.paulograbin.learningspringboot.chat;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;


@Service
@EnableBinding(ChatServiceStreams.class)
public class InboundChatService extends UserParsingHandshakeHandler {

    private final ChatServiceStreams chatServiceStreams;

    public InboundChatService(ChatServiceStreams chatServiceStreams) {
        this.chatServiceStreams = chatServiceStreams;
    }

    @Override
    protected Mono<Void> handleInternal(WebSocketSession session) {
        return session
                .receive()
                .log(getUser(session.getId()) + "-inbound-incoming-chat-message")
                .map(WebSocketMessage::getPayloadAsText)
                .log(getUser(session.getId()) + "-inbound-convert-to-text")
                .flatMap(message ->
                        broadcast(message, getUser(session.getId())))
                .log(getUser(session.getId()) + "inbound-broadcast-to-broker")
                .then();
    }

//    @Override
//    public Mono<Void> handle(WebSocketSession session) {
//        return session
//                .receive()
//                .log("inbound-incoming-chat-message")
//                .map(WebSocketMessage::getPayloadAsText)
//                .log("inbound-mark-with-session-id")
//                .map(s -> session.getId() + ": " + s)
//                .flatMap(this::broadcast)
//                .log("inbound-broadcast-to-broker")
//                .then();
//    }

        private Mono<?> broadcast(String message, String user) {
        return Mono.fromRunnable(() -> {
            chatServiceStreams.clientToBroker().send(
                    MessageBuilder
                            .withPayload(message)
                            .setHeader(ChatServiceStreams.USER_HEADER, user)
                            .build());
        });
    }

//    private Mono<?> broadcast(String message) {
//        return Mono.fromRunnable(() -> {
//            chatServiceStreams.clientToBroker().send(
//                    MessageBuilder
//                            .withPayload(message)
//                            .build());
//        });
//    }

    /**
     * @Service marks it as a Spring service that should launch automatically thanks to
     * Spring Boot's component scanning.
     *
     * @EnableBinding(ChatServiceStreams.class) signals Spring Cloud Stream to connect
     * this component to its broker-handling machinery.
     *
     * It implements the WebSocketHandler interface--when a client connects, the
     * handle(WebSocketSession) method will be invoked.
     *
     * Instead of using the @StreamListener annotation as in the previous code, this class
     * injects a ChatServiceStreams bean (same as the binding annotation) via
     * constructor injection.
     *
     * To handle a new WebSocketSession, we grab it and invoke its receive() method.
     * This hands us a Flux of potentially endless WebSocketMessage objects. These would
     * be the incoming messages sent in by the client that just connected. NOTE:
     * Every client that connects will invoke this method independently.
     *
     * We map the Flux<WebSocketMessage> object's stream of payload data into a
     * Flux<String> via getPayloadAsText().
     *
     * From there, we transform each raw message into a formatted message with the
     * WebSocket's session ID prefixing each message.
     *
     * Satisfied with our formatting of the message, we flatMap it onto our broadcast()
     * message in order to broadcast it to RabbitMQ.
     *
     * To hand control to the framework, we put a then() on the tail of this Reactor
     * flow so Spring can subscribe to this Flux.
     *
     * The broadcast method, invoked as every message is pulled down, marshals and
     * transmits the message by first building a Spring Cloud Streams Message<String>
     * object. It is pushed out over the ChatServiceStreams.clientToBroker() object's
     * MessageChannel via the send() API. To reactorize it, we wrap it with
     * Mono.fromRunnable.
     */
}
