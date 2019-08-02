package com.paulograbin.learningspringboot.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;


@Service
@EnableBinding(ChatServiceStreams.class)
public class OutboundCharService implements WebSocketHandler {

    private final static Logger log = LoggerFactory.getLogger(CommentService.class);
    private Flux<String> flux;
    private FluxSink<String> chatMessageSink;

    public OutboundCharService() {
        this.flux = Flux.<String>create(
                emitter -> this.chatMessageSink = emitter,
                FluxSink.OverflowStrategy.IGNORE)
                .publish()
                .autoConnect();
    }


    @StreamListener(ChatServiceStreams.BROKER_TO_CLIENT)
    public void listen(String message) {
        if (chatMessageSink != null) {
            log.info("Publishing " + message + " to websocket...");
            chatMessageSink.next(message);
        }
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session
                .send(this.flux
                        .map(session::textMessage)
                        .log("outbound-wrap-as-websocket-message"))
                .log("outbound-publish-to-websocket");
    }

    /**
     * Again, the @Service annotation marks this as an automatically wired Spring
     * service.
     *
     * It has the same EnableBinding(ChatServicesStreams.class) as the inbound service,
     * indicating that this, too, will participate with Spring Cloud Streams.
     * The constructor call wires up another one of those FluxSink objects, this time for
     * a Flux or strings.
     *
     * @StreamListener(ChatServiceStreams.BROKER_TO_CLIENT) indicates that this service
     * will be listening for incoming messages on the brokerToClient channel. When it
     * receives one, it will forward it to chatMessageSink.
     *
     * This class also implements WebSocketHandler, and each client attaches via the
     * handle(WebSocketSession) method. It is there that we connect the flux of incoming
     * messages to the WebSocketSession via its send() method.
     *
     * Because WebSocketSession.send() requires Flux<WebSocketMessage>, we map the
     * Flux<String> into it using session::textMessage. Nothing to serialize.
     *
     * There is a custom log flag when the Flux finished, and another for when the
     * entire Flux is handled.
     */
}
