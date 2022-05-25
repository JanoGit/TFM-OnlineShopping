package com.tfm.secureappspring.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(value = {"/", "Home", "Index"})
    public String showHomePage() {
        return "Index";
    }
}
