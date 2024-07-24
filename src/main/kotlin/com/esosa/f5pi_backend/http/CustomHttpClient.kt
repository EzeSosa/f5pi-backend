package com.esosa.f5pi_backend.http

import com.fasterxml.jackson.databind.JsonNode
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

    fun <T> doRequest(url: String, body: T, httpMethod: HttpMethod): JsonNode {
        val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
        val requestEntity = HttpEntity(body, headers)
        val response = restTemplate.exchange(
            url,
            httpMethod,
            requestEntity,
            JsonNode::class.java
        )

        return response.body
            ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "There was a problem with the request.")
    }
}