package com.greglturnquist.learningspringboot;

import com.greglturnquist.learningspringboot.images.Image;
import com.greglturnquist.learningspringboot.images.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;


@RestController
public class ImagesController {

    private static Logger log = LoggerFactory.getLogger(ImagesController.class);

    private static final String API_BASE_PATH = "/api";

    @Resource
    ImageService imageService;

    /**
     * Using the same Flux.just() helper, we return a rather contrived list
     * The Spring controller returns a Flux<Image> Reactor type, leaving Spring in
     * charge of properly subscribing to this flow when the time is right
     */
    @GetMapping(API_BASE_PATH + "/images")
    Flux<Image> images() {
        Hooks.onOperatorDebug();

        return imageService.findAllImages();
    }

    /**
     * @RequestBody instructs Spring to fetch data from the HTTP request body.
     * The container for our incoming data is another Flux of Image objects.
     * To consume the data, we map over it. In this case, we simply log it and pass the
     * original Image onto the next step of our flow.
     * To wrap this logging operation with a promise, we invoke Flux.then(), which
     * gives us Mono<Void>. Spring WebFlux will make good on this promise,
     * subscribing to the results when the client makes a request.
     *
     * Whether we send a single JSON item or an array of JSON items, Spring
     * WebFlux maps both onto Reactor's Flux with no issue. In classic Spring
     * MVC, we'd have to choose either Image or List<Image> and encode things
     * properly or write two handlers.
     *
     * http --json -v POST localhost:8080/api/images id=10 name=foo
     */
    @PostMapping(API_BASE_PATH + "/images")
    Mono<Void> create(@RequestBody Flux<Image> images) {
        return images
                .map(image -> {
                    log.info("We'll save " + image + " to a Reactive database soon!");
                    return image;
                })
                .then();
    }
}
