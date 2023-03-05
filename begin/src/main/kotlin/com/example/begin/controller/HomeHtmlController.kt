package com.example.begin.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeHtmlController {

    @GetMapping("/home")
    fun homePage(): String = "form/home"
}