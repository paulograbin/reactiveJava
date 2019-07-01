package com.greglturnquist.learningspringboot;

import com.greglturnquist.learningspringboot.images.CommentHelper;
import com.greglturnquist.learningspringboot.images.ImageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

import java.util.HashMap;


@Controller
public class HomeController {

    private final ImageService imageService;
    private final CommentHelper commentHelper;


    public HomeController(ImageService imageService, CommentHelper commentHelper) {
        this.imageService = imageService;
        this.commentHelper = commentHelper;
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
        model.addAttribute("images",
                imageService.findAllImages()
                        .map(image -> new HashMap<String, Object>() {{
                            put("id", image.getId());
                            put("name", image.getName());
                            put("comments", commentHelper.getComments(image.getId()));
                        }})
        );

        return Mono.just("index");
    }

    @RequestMapping("/greeting")
    @ResponseBody
    public String greeting(@RequestParam(required = false, defaultValue = "") String name) {
        return name.isEmpty() ? "Hey" : "Hey " + name + "!";
    }
}
