package com.greglturnquist.learningspringboot.comments;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;


public interface CustomProcessor {

    /**
     * It's a declarative interface.
     * It has two channel names, INPUT and OUTPUT. The INPUT channel uses the same as
     * Processor. To avoid colliding with the OUTPUT channel of Source, we create a
     * different channel name, emptyOutput. (Why call it emptyOutput? We'll see in a
     * moment!)
     * The is a SubscribableChannel for inputs and a MessageChannel for outputs.
     */

    String INPUT = "input";
    String OUTPUT = "emptyOutput";

    @Input(CustomProcessor.INPUT)
    SubscribableChannel input();

    @Output(CustomProcessor.OUTPUT)
    MessageChannel output();

}
