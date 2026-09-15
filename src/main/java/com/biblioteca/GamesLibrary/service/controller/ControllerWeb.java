package com.biblioteca.GamesLibrary.service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControllerWeb {
    @GetMapping("/")
    public String home(){
        return "index";
    }
}
