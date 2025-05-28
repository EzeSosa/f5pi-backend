package com.esosa.f5pi_backend.controllers.implementations

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/demo")
class DemoController {

    @GetMapping("ping")
    fun ping(): String = "pong"
}