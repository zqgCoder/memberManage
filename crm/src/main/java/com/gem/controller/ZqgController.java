package com.gem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/zqg")
public class ZqgController {

    @GetMapping("/pricing" )
    public String pricing(){
        return "zqg/pricing";
    }

    @GetMapping("/about")
    public String about(){
        return "zqg/about";
    }

    @GetMapping("/blog" )
    public String blog(){
        return "zqg/blog";
    }

    @GetMapping("/blog_single")
    public String blog_single(){
        return "zqg/blog_single";
    }

    @GetMapping("/contact")
    public String contact(){
        return "zqg/contact";
    }

    @GetMapping("/single_class")
    public String single_class(){
        return "zqg/single_class";
    }

    @GetMapping("/trainers")
    public String trainers(){
        return "zqg/trainers";
    }
}
