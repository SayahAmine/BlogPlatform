package org.core.blogplatform.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping({"/", "/posts"})
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/posts/new")
    public String newPost() {
        return "post-form";
    }

    @GetMapping("/posts/{id}")
    public String postDetail() {
        return "post-detail";
    }

    @GetMapping("/drafts")
    public String drafts() {
        return "drafts";
    }

    @GetMapping("/posts/{id}/edit")
    public String editPost() { return "post-form"; }
}