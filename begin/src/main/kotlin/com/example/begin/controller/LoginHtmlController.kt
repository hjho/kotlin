package com.example.begin.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LoginHtmlController {

    @GetMapping("/login")
    fun loginPage(): String = "form/login"
}