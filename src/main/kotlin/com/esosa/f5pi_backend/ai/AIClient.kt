package com.esosa.f5pi_backend.ai

import com.esosa.f5pi_backend.ai.requests.QueryRequest
import com.esosa.f5pi_backend.ai.responses.QueryResponse
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange

@HttpExchange
interface AIClient {
    @PostExchange("/generate-teams")
    fun generateTeams(@RequestBody queryRequest: QueryRequest): QueryResponse
}