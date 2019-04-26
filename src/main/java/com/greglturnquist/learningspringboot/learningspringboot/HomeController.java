package com.greglturnquist.learningspringboot.learningspringboot;


import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Controller
public class HomeController {


    private static final String BASE_PATH = "/images";
    private static final String FILENAME = "{filename:.+}";

    private final ImageService imageService;

    public HomeController(ImageService imageService) {
        this.imageService = imageService;
    }


    /**
     * addAttribute() lets us assign the image service's findAllImages() Flux to the
     * template model's images attribute.
     * The method returns "index" wrapped in a Mono, ensuring the whole thing is
     * chained together, top to bottom, to kick off when Spring WebFlux subscribes to
     * render the template.
     *
     * @param model
     * @return
     */
    @GetMapping("/")
    public Mono<String> index(Model model) {
        model.addAttribute("images", imageService.findAllImages());
        return Mono.just("index");
    }

    @RequestMapping("/greeting")
    @ResponseBody
    public String greeting(@RequestParam(required = false, defaultValue = "") String name) {
        return name.isEmpty() ? "Hey" : "Hey " + name + "!";
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
     */
    @PostMapping(value = BASE_PATH)
    public Mono<String> createFile(@RequestParam(name = "file") Flux<FilePart> files) {
        return imageService.createImage(files)
                .then(Mono.just("redirect:/"));
    }

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
