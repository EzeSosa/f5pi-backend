package com.esosa.f5pi_backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class F5piBackendApplication

fun main(args: Array<String>) {
	runApplication<F5piBackendApplication>(*args)
}