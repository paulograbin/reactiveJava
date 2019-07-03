package com.greglturnquist.learningspringboot.images;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

import java.io.IOException;


@Controller
public class UploadController {

    private static Logger log = LoggerFactory.getLogger(UploadController.class);

    private static final String API_BASE_PATH = "/api";
    private static final String BASE_PATH = "/images";
    private static final String FILENAME = "{filename:.+}";

    private final ImageService imageService;

    public UploadController(ImageService imageService) {
        this.imageService = imageService;
    }


    /**
     * Using the same Flux.just() helper, we return a rather contrived list
     * The Spring controller returns a Flux<Image> Reactor type, leaving Spring in
     * charge of properly subscribing to this flow when the time is right
     */
    @ResponseBody
    @GetMapping("/images")
    Flux<Image> images() {
        Hooks.onOperatorDebug();

        return imageService.findAllImages();
    }







    @GetMapping(value = BASE_PATH + "/" + FILENAME + "/raw", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public Mono<ResponseEntity<?>> oneRawImage(@PathVariable String filename) {
        return imageService.findOneImage(filename)
                .map(resource -> {
                    try {
                        return ResponseEntity.ok()
                                .contentLength(resource.contentLength())
                                .body(new InputStreamResource(
                                        resource.getInputStream()));
                    } catch (IOException e) {
                        return ResponseEntity.badRequest()
                                .body("Couldn't find " + filename + " => " + e.getMessage());
                    }
                });
    }

    /**
     *
     * A collection of incoming FilePart objects is represented as a Flux
     * The flux of files is handed directly to the image service to be processed
     * .then() indicates that once the method is complete, it will then return a
     * redirect:/ directive (wrapped in a Mono), issuing an HTML redirect to /
     *
     * http --json -v POST localhost:8080/api/images id=10 name=foo
     *
     */
    @PostMapping("/images1")
    public Mono<String> createFile(@RequestParam(name = "file") Flux<FilePart> files) {
        return imageService.createImage(files)
                .then(Mono.just("redirect:/"));
    }


//
//    /**
//     * @RequestBody instructs Spring to fetch data from the HTTP request body.
//     * The container for our incoming data is another Flux of Image objects.
//     * To consume the data, we map over it. In this case, we simply log it and pass the
//     * original Image onto the next step of our flow.
//     * To wrap this logging operation with a promise, we invoke Flux.then(), which
//     * gives us Mono<Void>. Spring WebFlux will make good on this promise,
//     * subscribing to the results when the client makes a request.
//     *
//     * Whether we send a single JSON item or an array of JSON items, Spring
//     * WebFlux maps both onto Reactor's Flux with no issue. In classic Spring
//     * MVC, we'd have to choose either Image or List<Image> and encode things
//     * properly or write two handlers.
//     *
//     *
//     */
//    @PostMapping("/images2")
//    Mono<Void> create(@RequestBody Flux<Image> images) {
//        return images
//                .map(image -> {
//                    log.info("We'll save " + image + " to a Reactive database soon!");
//                    return image;
//                })
//                .then();
//    }

    /**
     * Using Spring's @DeleteMapping annotation, this method is ready for HTTP DELETE
     * operations
     * It's keyed to the same BASE_PATH + "/" + FILENAME pattern
     * It taps the image service's deleteImage() method
     * It uses then() to wait until the delete is done before returning back a monowrapped redirect:/ directive
     *
     * @param filename
     * @return
     */
    @DeleteMapping(BASE_PATH + "/" + FILENAME)
    public Mono<String> deleteFile(@PathVariable String filename) {
        return imageService.deleteImage(filename)
                .then(Mono.just("redirect:/"));
    }

}
