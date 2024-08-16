package com.esosa.f5pi_backend.ai

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class AIClientConfig {
    @Value("\${ai.url}")
    lateinit var URL: String

    @Bean
    fun aiWebClient(): WebClient =
        WebClient.builder()
            .baseUrl(URL)
            .build()

    @Bean
    fun aiClient(): AIClient =
        HttpServiceProxyFactory.builder()
            .exchangeAdapter(WebClientAdapter.create(aiWebClient()))
            .build()
            .createClient(AIClient::class.java)
}