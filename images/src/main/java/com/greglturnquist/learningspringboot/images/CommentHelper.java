package com.greglturnquist.learningspringboot.images;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.List;


@Component
public class CommentHelper {

    private final RestTemplate restTemplate;

    public CommentHelper(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * restTemplate.exchange() is the generic method for making remote calls. There are
     * shortcuts such as getForObject() and getForEntity(), but when dealing with
     * generics (such as List<Comment>), we need to switch to exchange().
     * The first argument is the URL to the comments service that we just picked. It has
     * the port number we selected along with the route (/comments/{imageId}, a
     * template) where we can serve up a list of comments based on the image's ID.
     * The second argument is the HTTP verb we wish to use--GET.
     * The third argument is for headers and any body. Since this is a GET, there are
     * none.
     * The fourth argument is the return type of the data. Due to limitations of Java's
     * generics and type erasure, we have created a dedicated anonymous class to
     * capture the type details for List<Comment>, which Spring can use to interact with
     * Jackson to properly deserialize.
     * The final argument is the parameter (image.getId()) that will be used to expand
     * our URI template's {imageId} field.
     * Since exchange() returns a Spring ResponseEntity<T>, we need to invoke the body()
     * method to extract the response body.
     */
    @HystrixCommand(fallbackMethod = "defaultComments")
    public List<Comment> getComments(String imageId) {
        return restTemplate.exchange(
                "http://COMMENTS/comment/{imageId}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Comment>>() {
                },
                imageId).getBody();
    }

    public Flux<Comment> teste(Image image) {
        final WebClient webClient = WebClient.builder().baseUrl("http://COMMENTS/").build();

//        WebClient webClient = WebClient.create("http://COMMENTS/comments/{imageId}");
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("http://COMMENTS/comment/{imageId}")
                        .build(image.getId()))
                .retrieve().bodyToFlux(Comment.class);
    }

    public List<Comment> defaultComments(String imageId) {
        System.out.println("Returning fallback comments for image " + imageId);

//        return Collections.singletonList(new Comment("a", "b", "c"));
        return Collections.emptyList();
    }
}
