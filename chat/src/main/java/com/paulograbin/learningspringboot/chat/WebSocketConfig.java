package com.paulograbin.learningspringboot.chat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class WebSocketConfig {

    /**
     * @Bean indicates this entire method is used to construct a Spring bean.
     * It's a HandlerMapping bean, Spring's interface for linking routes with handler
     * methods.
     * The name of the method, webSocketMapping, indicates this method is about wiring
     * routes for WebSocket message handling.
     *
     * It asks for a copy of the CommentService bean we defined earlier. Since Spring
     * Boot activates component scanning, an instance of that service will be created
     * automatically, thanks to the @Service annotation we put on it earlier.
     * We create a Java Map, designed for mapping string-based routes onto
     * WebSocketHandler objects, and dub it a urlMap.
     * We load the map with /topic/comments.new, and link it with our CommentService, a
     * class that implements the WebSocketHandler interface.
     * There's the sticky issue of microservices, whereby, our chat service runs on a
     * different port from the frontend image service. Any modern web browser will
     * deny a web page calling a different port from the original port it was served. To
     * satisfy security restrictions (for now), we must implement a custom Crossorigin
     * Resource Sharing or CORS policy. In this case, we add an Allowed
     * Origin of http://localhost:8080, the address where the frontend image service
     * resides.
     * With both the urlMap and the corsConfiguration policy, we construct
     * SimpleUrlHandlerMapping. It also needs an order level of 10 to get viewed ahead of
     * certain other route handlers provided automatically by Spring Boot.
     */

    /**
     * Previously, this method only injected CommentService. Now we also inject
     * InboundChatService as well as OutboundChatService. These are two services we must
     * define based on the need to broker WebSocket messages between sessions.
     * (Don't panic! We'll get to that real soon).
     * We have two new routes added to the urlMap--/app/chatMessage.new and
     * /topic/chatMessage.new--which we just saw used in the web layer.
     * These same routes must also be added to our CORS policy.
     */
    @Bean
    public HandlerMapping webSocketMapping(CommentService commentService, InboundCharService inboundChatService, OutboundCharService outboundChatService) {
        Map<String, WebSocketHandler> urlMap = new HashMap<>();
        urlMap.put("/topic/comments.new", commentService);
        urlMap.put("/app/chatMessage.new", inboundChatService);
        urlMap.put("/topic/chatMessage.new", outboundChatService);

        Map<String, CorsConfiguration> corsConfigurationMap = new HashMap<>();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:8080");
        corsConfigurationMap.put("/topic/comments.new", corsConfiguration);
        corsConfigurationMap.put("/app/chatMessage.new", corsConfiguration);
        corsConfigurationMap.put("/topic/chatMessage.new", corsConfiguration);

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(10);
        mapping.setUrlMap(urlMap);
        mapping.setCorsConfigurations(corsConfigurationMap);

        return mapping;
    }

    @Bean
    WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
