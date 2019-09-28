package com.paulograbin.learningspringboot.chat;

import org.springframework.session.data.mongo.config.annotation.web.reactive.EnableMongoWebSession;


@EnableMongoWebSession
public class SessionConfig {

    /**
     * @EnableMongoWebSession activates Spring Session MongoDB, signaling to use
     * MongoDB as the place to read and write any session data
     */

}
