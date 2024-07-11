package com.esosa.f5pi_backend.http

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.MediaType

@Component
class CustomHttpClient {
    private val restTemplate = RestTemplate()

    fun doRequest(url: String, message: String): String {
        val requestBody = QueryRequest(message)
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val requestEntity = HttpEntity(requestBody, headers)
        val response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            requestEntity,
            String::class.java
        )

        return response.body
            ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    data class QueryRequest(val query: String)
}