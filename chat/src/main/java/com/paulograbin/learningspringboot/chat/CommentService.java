package com.paulograbin.learningspringboot.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;


@Service
@EnableBinding(Sink.class)
public class CommentService implements WebSocketHandler {

    private final static Logger log = LoggerFactory.getLogger(CommentService.class);

    private ObjectMapper mapper;
    private Flux<Comment> flux;
    private FluxSink<Comment> webSocketCommentSink;

    /**
     * We need a Jackson ObjectMapper, and will get it from Spring's container through
     * constructor injection.
     * To create a FluxSink that lets us put comments one by one onto a Flux, we use
     * Flux.create(), and let it initialize our sink, webSocketCommentSink.
     * When it comes to backpressure policy, it's wired to ignore backpressure signals
     * for simplicity's sake. There may be other scenarios where we would select
     * differently.
     * publish() and autoConnect() kick our Flux into action so that it's ready to start
     * transmitting once hooked into the WebSocket session.
     */
    public CommentService(ObjectMapper mapper) {
        this.mapper = mapper;
        this.flux = Flux.<Comment>create(
                emitter -> this.webSocketCommentSink = emitter,
                FluxSink.OverflowStrategy.IGNORE)
                .publish()
                .autoConnect();
    }

    /**
     * The broadcast() method is marked as a @StreamListener for Sink.INPUT. Messages
     * get deserialized as Comment objects thanks to the application/json setting.
     * The code checks if our webSocketCommentSink is null, indicating whether or not it's
     * been created.
     * A log message is printed.
     * The Comment is dropped into our webSocketSink, which means that it will become
     * available to our corresponding flux automatically.
     */
    @StreamListener(Sink.INPUT)
    public void broadcast(Comment comment) {
        if(webSocketCommentSink != null) {
            log.info("Publishing " + comment.toString() + " to websocket...");
            webSocketCommentSink.next(comment);
        }
    }

    /**
     * We are handed a WebSocketSession which has a very simple API
     * The Comment-based Flux is piped into the WebSocket via its send() method
     * This Flux itself is transformed from a series of Comment objects into a series of
     * JSON objects courtesy of Jackson, and then, finally, into a series of
     * WebSocketMessage objects
     */
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.send(this.flux
                .map(comment -> {
                    try {
                        return mapper.writeValueAsString(comment);

                    } catch (JsonProcessingException e) {
                        throw new RuntimeException();
                    }
                })
                .log("encode-as-json")
                .map(session::textMessage)
                .log("wrap as websocket message")
        )
                .log("publish to websocket");
    }
}
