package com.paulograbin.learningspringboot.chat;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;


public interface ChatServiceStreams {

    /**
     * Three channel names are defined at the top--NEW_COMMENTS, CLIENT_TO_BROKER, and
     * BROKER_TO_CLIENT. They each map onto a channel name of newComments,
     * clientToBroker, and brokerToClient.
     *
     * newComments() is defined as an input linked to the NEW_COMMENTS channel via the
     * @Input annotation, and has a return type of SubscribableChannel, meaning, it can
     * be used to consume messages.
     *
     * clientToBroker() is defined as an output linked to the CLIENT_TO_BROKER channel via
     * the @Output annotation, and has a return type of MessageChannel, which means that
     * it can be used to transmit messages.
     *
     * brokerToClient() is defined as an input linked to the BROKER_TO_CLIENT channel via
     * the @Input annotation, and also has a return type of SubscribableChannel, which
     * means it, too, can be used to consume messages.
     */

    String NEW_COMMENTS = "newComments";
    String CLIENT_TO_BROKER = "clientToBroker";
    String BROKER_TO_CLIENT = "brokerToClient";

    String USER_HEADER = "User";

    @Input(BROKER_TO_CLIENT)
    SubscribableChannel brokerToClient();

    @Input(NEW_COMMENTS)
    SubscribableChannel newComments();

    @Output(CLIENT_TO_BROKER)
    MessageChannel clientToBroker();

}
