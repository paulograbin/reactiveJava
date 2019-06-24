package com.greglturnquist.learningspringboot.comments;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.reactive.FluxSender;
import org.springframework.cloud.stream.reactive.StreamEmitter;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;


@Controller
/**
 * @EnableBinding(Source.class) flags this app as a source for new events. Spring
 * Cloud Stream uses this annotation to signal the creation of channels, which, in
 * RabbitMQ, translates to exchanges and queues.
 */
@EnableBinding(Source.class)
public class CommentController {

    private final MeterRegistry meterRegistry;
    private FluxSink<Message<Comment>> commentSink;
    private Flux<Message<Comment>> flux;

    public CommentController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        /**
         * The constructor proceeds to set up a FluxSink, the mechanism to emit new
         * messages into a downstream Flux. This sink is configured to ignore downstream
         * backpressure events. It starts publishing right away, autoconnecting to its
         * upstream source upon subscription.
         */
        this.flux = Flux.<Message<Comment>>create(
                emitter -> this.commentSink = emitter,
                FluxSink.OverflowStrategy.IGNORE)
                .publish()
                .autoConnect();
    }

    @PostMapping("/comment")
    public Mono<String> addComment(Mono<Comment> newComment) {
        if (commentSink != null) {
            return newComment
                    .map(comment -> commentSink.next(
                            MessageBuilder
                                    .withPayload(comment)
                                    .build())).then(Mono.just("redirect:/"));
        } else {
            return Mono.just("redirect:/");
        }
    }

    @StreamEmitter
    public void emit(@Output(Source.OUTPUT) FluxSender output) {
        output.send(this.flux);
    }
}
