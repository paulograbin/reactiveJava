package com.greglturnquist.learningspringboot.comments;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

import java.util.HashMap;


@Controller
public class HomeController {

    private CommentService commentService;

    public HomeController(CommentService commentService) {
        this.commentService = commentService;
    }


    @GetMapping("/")
    public Mono<String> index(Model model) {
        model.addAttribute("comments",
                commentService.findAllComments()
                        .map(comment -> new HashMap<String, Object>() {{
                            put("imageId", comment.getImageId());
                            put("id", comment.getId());
                            put("comment", comment.getComment());
                        }})
        );

        return Mono.just("index");
    }
}
