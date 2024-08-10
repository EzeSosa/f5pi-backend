package com.esosa.f5pi_backend.config

import com.cloudinary.Cloudinary
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CloudinaryConfig {

    @Value("\${cloudinary.cloud-name}") lateinit var CLOUD_NAME: String
    @Value("\${cloudinary.api-key}") lateinit var API_KEY: String
    @Value("\${cloudinary.api-secret}") lateinit var API_SECRET: String

    @Bean
    fun cloudinary(): Cloudinary =
        Cloudinary(
            hashMapOf(
                Pair("cloud_name", CLOUD_NAME),
                Pair("api_key", API_KEY),
                Pair("api_secret", API_SECRET)
            )
        )
}