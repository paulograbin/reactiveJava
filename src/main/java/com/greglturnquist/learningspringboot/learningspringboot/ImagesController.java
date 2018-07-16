package com.greglturnquist.learningspringboot.learningspringboot;

import org.springframework.web.bind.annotation.RestController;


@RestController
public class ImagesController {

//    private static Logger log = LoggerFactory.getLogger(ImagesController.class);
//
//    private static final String API_BASE_PATH = "api";
//
//    @GetMapping(API_BASE_PATH + "/images")
//    Flux<Image> images() {
//        return Flux.just(
//                new Image(1, "learning-spring-boot-edition-cover.jpg"),
//                new Image(2, "learning-spring-boot-2nd-edition-cover.jpg"),
//                new Image(3, "bazinga.jpg")
//        );
//    }
//
//    @PostMapping(API_BASE_PATH + "/images")
//    Mono<Void> create(@RequestBody Flux<Image> images) {
//        return images
//                .map(image -> {
//                    log.info("We'll save " + image + " to a Reactive database soon!");
//                    return image;
//                })
//                .then();
//    }
}
